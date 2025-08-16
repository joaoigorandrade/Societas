package com.example.Networking.Interfaces.Interceptors

import com.example.Networking.Core.NetworkResult
import io.ktor.client.request.*
import io.ktor.client.statement.*

interface NetworkInterceptor {
    suspend fun interceptRequest(request: HttpRequestBuilder): HttpRequestBuilder
    suspend fun interceptResponse(response: HttpResponse): HttpResponse
    suspend fun <T> interceptError(error: NetworkResult.Error): NetworkResult<T>
    
    val priority: Int get() = 0
}
