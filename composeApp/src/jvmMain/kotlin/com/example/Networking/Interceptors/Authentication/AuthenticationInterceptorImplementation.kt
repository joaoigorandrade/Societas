package com.example.Networking.Interceptors.Authentication

import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.Interceptors.AuthenticationInterceptor
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthenticationInterceptorImplementation(
    private val tokenProvider: TokenProvider
) : AuthenticationInterceptor {
    
    private val refreshMutex = Mutex()
    
    override val priority: Int = 100
    
    override suspend fun interceptRequest(request: HttpRequestBuilder): HttpRequestBuilder {
        return addAuthHeaders(request)
    }
    
    override suspend fun interceptResponse(response: HttpResponse): HttpResponse {
        if (response.status == HttpStatusCode.Unauthorized) {
            val refreshed = refreshTokenIfNeeded()
            if (!refreshed) {
                handleUnauthorized()
            }
        }
        return response
    }
    
    override suspend fun <T> interceptError(error: NetworkResult.Error): NetworkResult<T> {
        return error
    }
    
    override suspend fun addAuthHeaders(request: HttpRequestBuilder): HttpRequestBuilder {
        val token = tokenProvider.getAccessToken()
        if (token != null) {
            request.header(HttpHeaders.Authorization, "Bearer $token")
        }
        return request
    }
    
    override suspend fun refreshTokenIfNeeded(): Boolean {
        return refreshMutex.withLock {
            val refreshToken = tokenProvider.getRefreshToken()
            if (refreshToken != null) {
                try {
                    val newTokens = tokenProvider.refreshTokens(refreshToken)
                    tokenProvider.saveTokens(newTokens.accessToken, newTokens.refreshToken)
                    true
                } catch (_: Exception) {
                    false
                }
            } else {
                false
            }
        }
    }
    
    override suspend fun handleUnauthorized(): NetworkResult<Unit> {
        tokenProvider.clearTokens()
        return NetworkResult.Success(Unit)
    }
}
