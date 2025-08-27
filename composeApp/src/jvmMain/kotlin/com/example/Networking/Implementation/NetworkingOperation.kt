package com.example.Networking.Implementation

import com.example.Networking.Core.Configurations.NetworkConfiguration
import com.example.Networking.Interfaces.Client.Http.NetworkingOperationInterface
import com.example.Networking.Interfaces.RequestInterface
import com.example.Networking.RequestParameters
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class NetworkingOperation(
    private val config: NetworkConfiguration,
) : NetworkingOperationInterface {

    private val jsonSerializer = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = false
    }

    val client = io.ktor.client.HttpClient(CIO) {
        install(ContentNegotiation) {
            json(jsonSerializer)
        }

        if (config.enableLogging) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = config.timeout
            connectTimeoutMillis = config.connectTimeout
            socketTimeoutMillis = config.socketTimeout
        }

        defaultRequest {
            url(config.baseUrl)
            config.headers.forEach { (key, value) ->
                header(key, value)
            }
            // Add auth headers for non-auth requests
            if (!url.encodedPath.contains("auth")) {
                com.example.Domain.Models.Auth.UserSession.getToken()?.let { token ->
                    header("Authorization", "Bearer " + token)
                }
                com.example.Domain.Models.Auth.UserSession.getUserId()?.let { userId ->
                    header("X-User-Id", userId)
                }
            }
        }
    }

    override suspend fun executeRaw(request: RequestInterface): HttpResponse {
        return when (request.method) {
            HttpMethod.Get -> client.get(request.path) {
                request.headers?.forEach { (key, value) -> header(key, value) }
                when (val params = request.parameters) {
                    is RequestParameters.Query -> params.params.forEach { (k, v) -> url.parameters.append(k, v) }
                    else -> Unit
                }
            }

            HttpMethod.Post -> client.post(request.path) {
                request.headers?.forEach { (key, value) -> header(key, value) }
                contentType(ContentType.Application.Json)
                when (val params = request.parameters) {
                    is RequestParameters.Body -> setBody(params.data)
                    is RequestParameters.Query -> params.params.forEach { (k, v) -> url.parameters.append(k, v) }
                    is RequestParameters.None -> Unit
                }
            }

            HttpMethod.Put -> client.put(request.path) {
                request.headers?.forEach { (key, value) -> header(key, value) }
                contentType(ContentType.Application.Json)
                when (val params = request.parameters) {
                    is RequestParameters.Body -> setBody(params.data)
                    is RequestParameters.Query -> params.params.forEach { (k, v) -> url.parameters.append(k, v) }
                    is RequestParameters.None -> Unit
                }
            }

            HttpMethod.Delete -> client.delete(request.path) {
                request.headers?.forEach { (key, value) -> header(key, value) }
            }

            HttpMethod.Patch -> client.patch(request.path) {
                request.headers?.forEach { (key, value) -> header(key, value) }
                contentType(ContentType.Application.Json)
                when (val params = request.parameters) {
                    is RequestParameters.Body -> setBody(params.data)
                    is RequestParameters.Query -> params.params.forEach { (k, v) -> url.parameters.append(k, v) }
                    is RequestParameters.None -> Unit
                }
            }

            HttpMethod.Head -> client.head(request.path) {
                request.headers?.forEach { (key, value) -> header(key, value) }
            }

            HttpMethod.Options -> client.options(request.path) {
                request.headers?.forEach { (key, value) -> header(key, value) }
            }
            else -> client.request(request.path) {
                method = request.method
                request.headers?.forEach { (key, value) -> header(key, value) }
                when (val params = request.parameters) {
                    is RequestParameters.Body -> setBody(params.data)
                    is RequestParameters.Query -> params.params.forEach { (k, v) -> url.parameters.append(k, v) }
                    is RequestParameters.None -> Unit
                }
            }
        }
    }

    fun close() {
        client.close()
    }
}