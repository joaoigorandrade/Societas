package com.example.Networking.Interceptors.Authentication

import kotlinx.serialization.Serializable

@Serializable
data class TokenPair(
    val accessToken: String,
    val refreshToken: String?
)