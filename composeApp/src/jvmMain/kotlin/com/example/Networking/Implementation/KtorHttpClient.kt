package com.example.Networking.Implementation

import com.example.Networking.Core.Configurations.NetworkConfiguration
import com.example.Networking.Core.NetworkException
import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.HttpClient
import com.example.Networking.Interfaces.Interceptors.NetworkInterceptor
import com.example.Networking.Interfaces.StreamingHttpClient
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class KtorHttpClient(
    private val config: NetworkConfiguration,
    private val interceptors: List<NetworkInterceptor> = emptyList()
) : HttpClient, StreamingHttpClient {
    
    private val jsonSerializer = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = false
    }
    
    private val client = io.ktor.client.HttpClient(CIO) {
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
        
        install("CustomInterceptors") {
            interceptors.sortedByDescending { it.priority }.forEach { interceptor ->
                sendPipeline.intercept(HttpSendPipeline.Before) {
                    val modifiedContext = interceptor.interceptRequest(context)
                    proceedWith(modifiedContext)
                }
                
                receivePipeline.intercept(HttpReceivePipeline.After) { response ->
                    proceedWith(interceptor.interceptResponse(response))
                }
            }
        }
    }
    
    override suspend fun <T> get(
        endpoint: String,
        headers: Map<String, String>,
        queryParams: Map<String, String>
    ): NetworkResult<T> = safeNetworkCall {
        val response = client.get(endpoint) {
            headers.forEach { (key, value) -> header(key, value) }
            queryParams.forEach { (key, value) -> parameter(key, value) }
        }
        @Suppress("UNCHECKED_CAST")
        response.bodyAsText() as T
    }
    
    override suspend fun <T, R> post(
        endpoint: String,
        body: T,
        headers: Map<String, String>
    ): NetworkResult<R> = safeNetworkCall {
        val response = client.post(endpoint) {
            headers.forEach { (key, value) -> header(key, value) }
            contentType(ContentType.Application.Json)
            when (body) {
                is String -> setBody(body)
                is ByteArray -> setBody(body)
                else -> setBody(body.toString())
            }
        }
        @Suppress("UNCHECKED_CAST")
        response.bodyAsText() as R
    }
    
    override suspend fun <T, R> put(
        endpoint: String,
        body: T,
        headers: Map<String, String>
    ): NetworkResult<R> = safeNetworkCall {
        val response = client.put(endpoint) {
            headers.forEach { (key, value) -> header(key, value) }
            contentType(ContentType.Application.Json)
            when (body) {
                is String -> setBody(body)
                is ByteArray -> setBody(body)
                else -> setBody(body.toString())
            }
        }
        @Suppress("UNCHECKED_CAST")
        response.bodyAsText() as R
    }
    
    override suspend fun <T, R> patch(
        endpoint: String,
        body: T,
        headers: Map<String, String>
    ): NetworkResult<R> = safeNetworkCall {
        val response = client.patch(endpoint) {
            headers.forEach { (key, value) -> header(key, value) }
            contentType(ContentType.Application.Json)
            when (body) {
                is String -> setBody(body)
                is ByteArray -> setBody(body)
                else -> setBody(body.toString())
            }
        }
        @Suppress("UNCHECKED_CAST")
        response.bodyAsText() as R
    }
    
    override suspend fun delete(
        endpoint: String,
        headers: Map<String, String>
    ): NetworkResult<Unit> = safeNetworkCall {
        client.delete(endpoint) {
            headers.forEach { (key, value) -> header(key, value) }
        }
        Unit
    }
    
    override suspend fun <T> head(
        endpoint: String,
        headers: Map<String, String>
    ): NetworkResult<Map<String, List<String>>> = safeNetworkCall {
        val response = client.head(endpoint) {
            headers.forEach { (key, value) -> header(key, value) }
        }
        response.headers.toMap()
    }
    
    override suspend fun <T> options(
        endpoint: String,
        headers: Map<String, String>
    ): NetworkResult<Map<String, List<String>>> = safeNetworkCall {
        val response = client.options(endpoint) {
            headers.forEach { (key, value) -> header(key, value) }
        }
        response.headers.toMap()
    }
    
    override fun <T> getStream(
        endpoint: String,
        headers: Map<String, String>
    ): Flow<NetworkResult<T>> = flow {
        try {
            client.prepareGet(endpoint) {
                headers.forEach { (key, value) -> header(key, value) }
            }.execute { response ->
                if (response.status.isSuccess()) {
                    @Suppress("UNCHECKED_CAST")
                    val data: T = response.bodyAsText() as T
                    emit(NetworkResult.Success(data))
                } else {
                    emit(NetworkResult.Error(
                        NetworkException.HttpError(
                            response.status.value,
                            response.status.description
                        )
                    ))
                }
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(mapException(e)))
        }
    }
    
    override suspend fun <T> uploadFile(
        endpoint: String,
        fileData: ByteArray,
        fileName: String,
        mimeType: String,
        headers: Map<String, String>,
        onProgress: (Float) -> Unit
    ): NetworkResult<T> = safeNetworkCall {
        val response = client.post(endpoint) {
            headers.forEach { (key, value) -> header(key, value) }
            setBody(fileData)
            contentType(ContentType.parse(mimeType))
            
            onUpload { bytesSentTotal, contentLength ->
                if (contentLength != null) {
                    val progress = bytesSentTotal.toFloat() / contentLength.toFloat()
                    onProgress(progress)
                }
            }
        }
        @Suppress("UNCHECKED_CAST")
        response.bodyAsText() as T
    }
    
    override suspend fun downloadFile(
        endpoint: String,
        headers: Map<String, String>,
        onProgress: (Float) -> Unit
    ): NetworkResult<ByteArray> = safeNetworkCall {
        val response = client.get(endpoint) {
            headers.forEach { (key, value) -> header(key, value) }
            
            onDownload { bytesSentTotal, contentLength ->
                if (contentLength != null) {
                    val progress = bytesSentTotal.toFloat() / contentLength.toFloat()
                    onProgress(progress)
                }
            }
        }
        response.body<ByteArray>()
    }

    private suspend fun <T> safeNetworkCall(call: suspend () -> T): NetworkResult<T> {
        return try {
            val result = call()
            NetworkResult.Success(result)
        } catch (e: Exception) {
            var error = NetworkResult.Error(mapException(e))
            
            interceptors.forEach { interceptor ->
                error = interceptor.interceptError<T>(error) as NetworkResult.Error
            }
            
            error
        }
    }
    
    private fun mapException(exception: Exception): NetworkException {
        return when (exception) {
            is ClientRequestException -> NetworkException.HttpError(
                exception.response.status.value,
                exception.message ?: "Client error"
            )
            is ServerResponseException -> NetworkException.HttpError(
                exception.response.status.value,
                exception.message ?: "Server error"
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