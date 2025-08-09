package com.example.Networking.Interceptors

import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.Interceptors.AuthenticationInterceptor
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthenticationInterceptorImpl(
    private val tokenProvider: TokenProvider
) : AuthenticationInterceptor {
    
    private val refreshMutex = Mutex()
    
    override val priority: Int = 100 // High priority
    
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
        return error // Pass through for now
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
                } catch (e: Exception) {
                    false
                }
            } else {
                false
            }
        }
    }
    
    override suspend fun handleUnauthorized(): NetworkResult<Unit> {
        tokenProvider.clearTokens()
        // Here you could emit an event to navigate to login screen
        return NetworkResult.Success(Unit)
    }
}
interface TokenProvider {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(accessToken: String, refreshToken: String?)
    suspend fun refreshTokens(refreshToken: String): TokenPair
    suspend fun clearTokens()
}
data class TokenPair(
    val accessToken: String,
    val refreshToken: String?
)
