package com.example.Networking.Interceptors.Authentication

import com.example.Networking.Interceptors.Authentication.TokenPair

interface TokenProvider {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(accessToken: String, refreshToken: String?)
    suspend fun refreshTokens(refreshToken: String): TokenPair
    suspend fun clearTokens()
}