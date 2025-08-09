package com.example.Networking.Interfaces.Interceptors

import com.example.Networking.Core.NetworkResult
import io.ktor.client.request.*
import io.ktor.client.statement.*

interface AuthenticationInterceptor : NetworkInterceptor {
    suspend fun addAuthHeaders(request: HttpRequestBuilder): HttpRequestBuilder
    suspend fun refreshTokenIfNeeded(): Boolean
    suspend fun handleUnauthorized(): NetworkResult<Unit>
}