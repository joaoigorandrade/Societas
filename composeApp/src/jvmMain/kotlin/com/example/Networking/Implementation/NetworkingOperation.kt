package com.example.Networking.Implementation

import com.example.Networking.Core.Configurations.NetworkConfiguration
import com.example.Networking.Core.NetworkException
import com.example.Networking.Core.NetworkResult
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
        }
    }

    override suspend fun <T> execute(request: RequestInterface): NetworkResult<T> = safeNetworkCall {
        val response = when (request.method) {
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
        @Suppress("UNCHECKED_CAST")
        response.bodyAsText() as T
    }

    private suspend fun <T> safeNetworkCall(call: suspend () -> T): NetworkResult<T> {
        return try {
            val result = call()
            NetworkResult.Success(result)
        } catch (e: Exception) {
            val error = NetworkResult.Error(mapException(e))
            error
        }
    }

    private fun mapException(exception: Exception): NetworkException {
        return when (exception) {
            is ClientRequestException -> NetworkException.HttpError(
                exception.response.status.value,
                exception.message
            )

            is ServerResponseException -> NetworkException.HttpError(
                exception.response.status.value,
                exception.message
            )

            is HttpRequestTimeoutException -> NetworkException.TimeoutError(
                exception.message ?: "Request timeout"
            )

            else -> NetworkException.UnknownError(
                exception.message ?: "Unknown error occurred"
            )
        }
    }
    fun close() {
        client.close()
    }
}