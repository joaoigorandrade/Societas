package com.example.Networking

/**
 * # Societas Networking Layer
 * 
 * A modular, SOLID-principle-based networking layer built with Ktor client.
 * 
 * ## Features
 * 
 * ### ✅ Real-time Communication
 * - WebSocket client with automatic reconnection
 * - Event-driven message handling
 * - Connection state management
 * - Structured and text message support
 * 
 * ### ✅ Complete REST API Support
 * - All HTTP methods (GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS)
 * - Request/Response interceptors
 * - Automatic retry with exponential backoff
 * - Error handling with custom exception types
 * 
 * ### ✅ Scalability & Modularity
 * - Interface-based design following SOLID principles
 * - Dependency injection with Koin
 * - Pluggable interceptors for cross-cutting concerns
 * - Streaming support for large files
 * 
 * ## Architecture
 * 
 * ```
 * Domain Layer (Use Cases)
 *        ↓
 * Repository Layer (Abstractions)
 *        ↓
 * Networking Layer (Implementations)
 *        ↓
 * Ktor Client (HTTP/WebSocket)
 * ```
 * 
 * ## Core Components
 * 
 * ### 1. Network Result
 * ```kotlin
 * sealed class NetworkResult<out T> {
 *     data class Success<T>(val data: T) : NetworkResult<T>()
 *     data class Error(val exception: NetworkException) : NetworkResult<Nothing>()
 *     data class Loading(val isLoading: Boolean) : NetworkResult<Nothing>()
 * }
 * ```
 * 
 * ### 2. HTTP Client Interface
 * ```kotlin
 * interface HttpClient {
 *     suspend fun <T> get(endpoint: String, headers: Map<String, String>, queryParams: Map<String, String>): NetworkResult<T>
 *     suspend fun <T, R> post(endpoint: String, body: T, headers: Map<String, String>): NetworkResult<R>
 *     // ... other HTTP methods
 * }
 * ```
 * 
 * ### 3. WebSocket Client Interface
 * ```kotlin
 * interface WebSocketClient {
 *     suspend fun connect(): NetworkResult<Unit>
 *     suspend fun <T> sendMessage(message: T): NetworkResult<Unit>
 *     fun <T> observeMessages(): Flow<NetworkResult<T>>
 *     fun observeConnectionState(): Flow<WebSocketState>
 * }
 * ```
 * 
 * ## Quick Start
 * 
 * ### 1. Basic HTTP Request
 * ```kotlin
 * class UserRepository(private val httpClient: HttpClient) {
 *     suspend fun getUser(id: String): NetworkResult<User> {
 *         return httpClient.get<User>("/users/$id")
 *     }
 * }
 * ```
 * 
 * ### 2. WebSocket Communication
 * ```kotlin
 * class ChatService(private val webSocketClient: WebSocketClient) {
 *     suspend fun startChat() {
 *         webSocketClient.connect()
 *         
 *         webSocketClient.observeMessages<ChatMessage>()
 *             .collect { result ->
 *                 result.onSuccess { message ->
 *                     println("Received: ${message.content}")
 *                 }
 *             }
 *     }
 * }
 * ```
 * 
 * ### 3. File Upload with Progress
 * ```kotlin
 * class FileService(private val streamingClient: StreamingHttpClient) {
 *     suspend fun uploadFile(data: ByteArray): NetworkResult<String> {
 *         return streamingClient.uploadFile(
 *             endpoint = "/files/upload",
 *             fileData = data,
 *             fileName = "document.pdf",
 *             mimeType = "application/pdf",
 *             onProgress = { progress -> 
 *                 println("Upload progress: ${(progress * 100).toInt()}%")
 *             }
 *         )
 *     }
 * }
 * ```
 * 
 * ## Dependency Injection Setup
 * 
 * The networking layer is fully integrated with Koin:
 * 
 * ```kotlin
 * val appModule = module {
 *     includes(
 *         networkModule,  // <-- Networking layer
 *         domainModule,
 *         serviceModule,
 *         uiModule
 *     )
 * }
 * ```
 * 
 * ## Configuration
 * 
 * ### Development Configuration
 * ```kotlin
 * NetworkConfig.development("http://localhost:8080")
 * ```
 * 
 * ### Production Configuration
 * ```kotlin
 * NetworkConfig.production("https://api.societas.com")
 * ```
 * 
 * ### WebSocket Configuration
 * ```kotlin
 * WebSocketConfig(
 *     url = "wss://api.societas.com/ws",
 *     pingInterval = 20_000L,
 *     reconnectAttempts = 5
 * )
 * ```
 * 
 * ## Interceptors
 * 
 * The networking layer supports pluggable interceptors:
 * 
 * ### Authentication
 * - Automatic token injection
 * - Token refresh handling
 * - Unauthorized response handling
 * 
 * ### Logging
 * - Request/response logging
 * - Configurable log levels
 * - Sensitive data masking
 * 
 * ### Retry
 * - Automatic retry with exponential backoff
 * - Configurable retry conditions
 * - Network error resilience
 * 
 * ## Error Handling
 * 
 * ```kotlin
 * when (val result = apiRepository.getUser("123")) {
 *     is NetworkResult.Success -> {
 *         val user = result.data
 *         // Handle success
 *     }
 *     is NetworkResult.Error -> {
 *         when (result.exception) {
 *             is NetworkException.HttpError -> {
 *                 // Handle HTTP errors (4xx, 5xx)
 *             }
 *             is NetworkException.NetworkUnavailable -> {
 *                 // Handle network connectivity issues
 *             }
 *             is NetworkException.TimeoutError -> {
 *                 // Handle timeout
 *             }
 *             // ... other error types
 *         }
 *     }
 *     is NetworkResult.Loading -> {
 *         // Show loading indicator
 *     }
 * }
 * ```
 * 
 * ## Extension and Customization
 * 
 * ### Custom Interceptors
 * ```kotlin
 * class CustomInterceptor : NetworkInterceptor {
 *     override suspend fun interceptRequest(request: HttpRequestBuilder): HttpRequestBuilder {
 *         // Modify outgoing requests
 *         return request
 *     }
 *     
 *     override suspend fun interceptResponse(response: HttpResponse): HttpResponse {
 *         // Handle incoming responses
 *         return response
 *     }
 * }
 * ```
 * 
 * ### Custom Token Provider
 * ```kotlin
 * class SecureTokenProvider : TokenProvider {
 *     override suspend fun getAccessToken(): String? {
 *         // Implement secure token storage
 *     }
 *     
 *     override suspend fun saveTokens(accessToken: String, refreshToken: String?) {
 *         // Implement secure token persistence
 *     }
 * }
 * ```
 * 
 * ## Testing
 * 
 * The interface-based design makes testing straightforward:
 * 
 * ```kotlin
 * class MockHttpClient : HttpClient {
 *     override suspend fun <T> get(endpoint: String, headers: Map<String, String>, queryParams: Map<String, String>): NetworkResult<T> {
 *         // Return mock data
 *         return NetworkResult.Success(mockData as T)
 *     }
 * }
 * ```
 * 
 * ## Best Practices
 * 
 * 1. **Use Repository Pattern**: Don't inject networking clients directly into use cases
 * 2. **Handle All Result Types**: Always handle Success, Error, and Loading states
 * 3. **Configure Timeouts**: Set appropriate timeouts for your use case
 * 4. **Implement Proper Token Storage**: Use secure storage for authentication tokens
 * 5. **Monitor Network State**: Observe connection states for real-time features
 * 6. **Use Interceptors**: Leverage interceptors for cross-cutting concerns
 * 7. **Test with Mocks**: Use interface-based mocking for unit tests
 * 
 * ## Performance Considerations
 * 
 * - Connection pooling is handled by Ktor CIO engine
 * - Automatic retry prevents unnecessary failures
 * - Streaming support for large file operations
 * - WebSocket connection reuse for real-time features
 * - Configurable timeouts prevent resource leaks
 * 
 * ## Security Features
 * 
 * - HTTPS/WSS support
 * - Token-based authentication
 * - Automatic token refresh
 * - Sensitive header masking in logs
 * - Request/response validation
 */
