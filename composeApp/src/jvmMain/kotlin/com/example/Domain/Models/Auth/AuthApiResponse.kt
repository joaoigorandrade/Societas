package com.example.Domain.Models.Auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthApiResponse(
    val success: Boolean,
    val message: String? = null,
    val data: AuthSuccessData? = null,
    val error: AuthErrorPayload? = null
)

@Serializable
data class AuthSuccessData(
    val user: UserData,
    val token: String,
    val refreshToken: String? = null
)

@Serializable
data class UserData(
    val uid: String,
    val email: String,
    val emailVerified: Boolean,
    val displayName: String? = null,
    val createdAt: String? = null, 
    val lastLoginAt: String? = null
)

@Serializable
data class AuthErrorPayload(
    val email: String?,
    val password: String?
)
