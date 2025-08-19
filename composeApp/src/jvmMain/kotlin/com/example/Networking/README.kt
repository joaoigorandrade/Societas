package com.example.Networking

/*
README — Networking Layer

Overview
The networking layer provides a simple, DI-friendly abstraction over Ktor’s HttpClient to execute HTTP requests and return results as a sealed type (NetworkResult). It is designed to separate concerns:
- UI/ViewModel triggers a UseCase
- UseCase calls a Repository
- Repository delegates to a NetworkingOperationInterface
- NetworkingOperation implements the HTTP details using Ktor

Architecture flow
UI (ViewModel/Screen) → UseCase → Repository → NetworkingOperationInterface → NetworkingOperation (Ktor) → Server
- DI: Koin modules wire everything together (DomainModule, NetworkModule)
- Lifecycle: NetworkManager handles cleanup by closing the HttpClient instance

Key types and files
- RequestInterface (Interfaces/Request/RequestInterface.kt)
  • method: HttpMethod
  • path: String (relative to baseUrl)
  • parameters: RequestParameters (Query, Body, None)
  • headers: Map<String, String>?
- RequestParameters (Interfaces/Request/RequestParameters.kt)
  • Query(Map<String, String>) → appended to URL
  • Body(Any) → sent as JSON when applicable
  • None
- SocietasRequest (Domain/SocietasRequest.kt)
  • Example enum implementing RequestInterface (e.g., Home)
- NetworkingOperationInterface (Interfaces/Client/Http)
  • suspend fun <T> execute(request: RequestInterface): NetworkResult<T>
- NetworkingOperation (Implementation)
  • Builds Ktor HttpClient(CIO) with ContentNegotiation(Json), Logging (optional), HttpTimeout, and defaultRequest (baseUrl + headers)
  • Handles all HTTP methods (GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, fallback)
  • Injects per-request headers and RequestParameters (Query/Body)
  • Wraps result via safeNetworkCall into NetworkResult.Success or NetworkResult.Error
  • Maps exceptions to NetworkException: HttpError, TimeoutError, UnknownError
  • Currently returns response.bodyAsText() cast to T (see Robustness section)
- NetworkResult (Core/NetworkResult.kt)
  • Success<T>(data), Error(exception, message), Loading
  • Helpers: onSuccess, onError, map
- NetworkException (Core/NetworkException.kt)
  • NetworkUnavailable, HttpError(code, message), SerializationError, TimeoutError, UnknownError
- NetworkConfiguration (Core/Configurations/NetworkConfiguration.kt)
  • baseUrl, timeouts, retryAttempts, retryDelay, enableLogging, headers
  • factory presets: default, development, production
- NetworkModule (DependencyInjection/NetworkModule.kt)
  • Provides configurations and binds NetworkingOperation as NetworkingOperationInterface
- NetworkManager (Manager/NetworkManager.kt)
  • Owns the operation interface and closes the client on cleanup
- ApiRepository and ApiRepositoryInterface (Domain/Repository)
  • Repository delegates execute(request) to NetworkingOperationInterface
- Use case example: SocietasHomeUseCase (Domain/UseCase/Home)
  • Adds its own timeout and retry loop, and validates data before emitting a UI state

Execution summary
1) UseCase calls repository.execute(SocietasRequest.Home)
2) Repository forwards to NetworkingOperation.execute
3) NetworkingOperation builds the request (baseUrl + path), applies headers, and injects parameters
4) The HTTP response is read (currently as text) and wrapped into NetworkResult.Success
5) Exceptions are caught and mapped to NetworkResult.Error with a NetworkException subtype

Using the networking layer
- Define a new request
  enum class MyRequest: RequestInterface {
    UserById {
      override val method = io.ktor.http.HttpMethod.Get
      override val path = "/users"
      override val parameters = RequestParameters.Query(mapOf("id" to "123"))
      override val headers: Map<String, String>? = null
    }
  }

- Execute from a use case or repository consumer
  val result: com.example.Networking.Core.NetworkResult<User> = repository.execute(MyRequest.UserById)
  when (result) {
    is com.example.Networking.Core.NetworkResult.Success -> {
      val user = result.data
      // handle success
    }
    is com.example.Networking.Core.NetworkResult.Error -> {
      // inspect result.exception
    }
    is com.example.Networking.Core.NetworkResult.Loading -> { /* optional */ }
  }

- Example POST with body
  @kotlinx.serialization.Serializable
  data class CreateUserBody(val name: String)

  enum class MyRequest: RequestInterface {
    CreateUser {
      override val method = io.ktor.http.HttpMethod.Post
      override val path = "/users"
      override val parameters = RequestParameters.Body(CreateUserBody("Ana"))
      override val headers = mapOf("X-Feature" to "alpha")
    }
  }

Dependency Injection
- NetworkModule exposes NetworkConfiguration via named qualifiers ("development", "production")
- NetworkingOperation is created with the selected config and injected wherever NetworkingOperationInterface is required
- NetworkManager.cleanup closes the HttpClient (important to prevent leaks)

What is missing to make it more robust
1) Typed deserialization (high impact)
   - Instead of response.bodyAsText() as T, leverage Ktor’s ContentNegotiation to deserialize strongly typed models: val model: T = response.body()
   - Make execute inline with reified T, or accept a KSerializer<T> when generics are erased
   - Catch kotlinx.serialization.SerializationException and map to NetworkException.SerializationError

2) Error handling and error body parsing
   - Parse structured error payloads (e.g., { code, message, details }) into a domain ErrorEnvelope
   - Include HTTP status code, request id (from headers), endpoint, and truncated raw body in NetworkException for easier debugging
   - Respect Retry-After on 429, and provide user-friendly messages for common error classes

3) Retries and backoff strategy
   - Use Ktor HttpRequestRetry plugin or custom retry with exponential backoff + jitter
   - Retry only idempotent methods (GET/HEAD/PUT/options depending on server semantics) and on transient failures (timeouts, 5xx, network I/O)
   - Avoid retrying on 4xx (except 408/429 with policy)

4) Timeouts and cancellation
   - Align client timeouts with use-case timeouts to avoid double timeouts and race conditions
   - Map TimeoutCancellationException to TimeoutError explicitly
   - Ensure coroutine cancellation is propagated and never masked

5) Authentication and authorization
   - Add an interceptor to inject Authorization headers (Bearer tokens) from a secure store
   - Implement automatic token refresh on 401 with a single-flight/mutex mechanism; replay the original request after refresh
   - Avoid infinite refresh loops; clear session and signal logout on persistent 401/403

6) Connectivity checks and circuit breaker
   - Integrate a reachability check (platform-specific) and surface NetworkUnavailable early
   - Implement a circuit breaker to stop hammering failing endpoints; half-open after a cool-down to probe recovery

7) Caching
   - Honor ETag/If-None-Match and Cache-Control response directives
   - Provide a pluggable cache (memory/disk) with strategies (networkFirst, cacheFirst)

8) Observability and logging
   - Structured logs with redaction of Authorization, cookies, and PII
   - Correlation IDs (request-id) propagation; metrics (latency, error rate) and tracing (OpenTelemetry) hooks

9) File transfer and streaming
   - Support multipart uploads, streamed downloads with progress, and WebSockets where relevant

10) Configuration and environments
   - Externalize env values (.env/build flavors); enable switching baseUrl and toggling logging/security policies
   - Per-request overrides for timeout/retry when necessary

11) Testing
   - Unit tests using Ktor MockEngine for success, error, timeout, and retry scenarios
   - Integration tests against local server; contract tests to validate schemas and breaking changes

12) Security
   - TLS configuration and certificate pinning; strict hostname verification
   - Secure token storage; prevent logging of secrets; validate redirects and mixed content policies

13) Multiplatform readiness
   - Abstract engine selection for other targets (Darwin, Android) and consider expect/actual if needed

14) API ergonomics
   - Provide a small DSL/builder for RequestInterface (easier to compose and test)
   - Support per-request interceptors and post-processors
   - Consistent adoption of NetworkResult.Loading at repository level if desired by UI

Migration hints for typed deserialization
- Change NetworkingOperation.execute to an inline reified function: suspend inline fun <reified T> execute(...)
- Replace bodyAsText() with response.body<T>()
- Map SerializationException to NetworkException.SerializationError
- If server contracts and UI models differ, add a mapper layer (DTO → Domain)

This README.kt is a living document for the networking layer. Keep it updated as the implementation evolves.
*/