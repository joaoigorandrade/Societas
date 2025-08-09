package com.example.Networking.Interceptors

import com.example.Networking.Core.NetworkException
import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.Interceptors.RetryInterceptor
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetryInterceptorImpl(
    override val maxRetries: Int = 3,
    private val baseDelay: Long = 1000L,
    private val backoffMultiplier: Double = 2.0,
    private val retryableStatusCodes: Set<HttpStatusCode> = setOf(
        HttpStatusCode.InternalServerError,
        HttpStatusCode.BadGateway,
        HttpStatusCode.ServiceUnavailable,
        HttpStatusCode.GatewayTimeout
    )
) : RetryInterceptor {
    
    override val priority: Int = 10
    
    override suspend fun interceptRequest(request: HttpRequestBuilder): HttpRequestBuilder {
        return request
    }
    
    override suspend fun interceptResponse(response: HttpResponse): HttpResponse {
        return response
    }
    
    override suspend fun <T> interceptError(error: NetworkResult.Error): NetworkResult<T> {
        return error
    }
    
    override suspend fun shouldRetry(attempt: Int, error: Exception): Boolean {
        if (attempt >= maxRetries) return false
        
        return when (error) {
            is ConnectException,
            is UnknownHostException,
            is SocketTimeoutException,
            is ConnectTimeoutException -> true
            
            is HttpRequestTimeoutException -> true
            
            is ConnectTimeoutException -> true
            
            is ServerResponseException -> {
                retryableStatusCodes.contains(error.response.status)
            }
            
            is ClientRequestException -> false
            
            else -> false
        }
    }
    
    override suspend fun getRetryDelay(attempt: Int): Long {
        return (baseDelay * Math.pow(backoffMultiplier, attempt.toDouble())).toLong()
    }
    
    suspend fun <T> executeWithRetry(
        block: suspend () -> T
    ): T {
        var lastException: Exception? = null
        
        repeat(maxRetries + 1) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                lastException = e
                
                if (attempt < maxRetries && shouldRetry(attempt, e)) {
                    val delayTime = getRetryDelay(attempt)
                    delay(delayTime)
                } else {
                    throw e
                }
            }
        }
        
        throw lastException ?: RuntimeException("Unexpected retry failure")
    }
}
