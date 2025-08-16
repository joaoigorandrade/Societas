package com.example.Networking.Token

import com.example.Networking.Interceptors.Authentication.TokenProvider
import com.example.Networking.Interceptors.Authentication.TokenPair

class InMemoryTokenProvider : TokenProvider {
    private var accessToken: String? = null
    private var refreshToken: String? = null
    
    override suspend fun getAccessToken(): String? = accessToken
    
    override suspend fun getRefreshToken(): String? = refreshToken
    
    override suspend fun saveTokens(accessToken: String, refreshToken: String?) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }
    
    override suspend fun refreshTokens(refreshToken: String): TokenPair {
        return TokenPair(
            accessToken = "new_access_token",
            refreshToken = "new_refresh_token"
        )
    }
    
    override suspend fun clearTokens() {
        accessToken = null
        refreshToken = null
    }
}