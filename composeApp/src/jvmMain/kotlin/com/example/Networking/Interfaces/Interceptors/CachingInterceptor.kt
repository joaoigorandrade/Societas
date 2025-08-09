package com.example.Networking.Interfaces.Interceptors

import com.example.Networking.Interfaces.Interceptors.NetworkInterceptor
import io.ktor.client.request.*
import io.ktor.client.statement.*

interface CachingInterceptor : NetworkInterceptor {
    suspend fun getCachedResponse(request: HttpRequestBuilder): HttpResponse?
    suspend fun cacheResponse(request: HttpRequestBuilder, response: HttpResponse)
    suspend fun clearCache()
}