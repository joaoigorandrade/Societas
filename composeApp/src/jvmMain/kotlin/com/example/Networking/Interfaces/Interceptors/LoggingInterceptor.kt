package com.example.Networking.Interfaces.Interceptors

import com.example.Networking.Interfaces.Interceptors.NetworkInterceptor
import io.ktor.client.request.*
import io.ktor.client.statement.*

interface LoggingInterceptor : NetworkInterceptor {
    suspend fun logRequest(request: HttpRequestBuilder)
    suspend fun logResponse(response: HttpResponse)
    suspend fun logError(error: Exception)
}