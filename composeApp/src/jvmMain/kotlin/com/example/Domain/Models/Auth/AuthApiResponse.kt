package com.example.Domain.Models.Auth

import kotlinx.serialization.SerialName
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
    val createdAt: FirestoreTimestamp? = null,
    val lastLoginAt: FirestoreTimestamp? = null
)

@Serializable
data class AuthErrorPayload(
    val email: String?,
    val password: String?
)

@Serializable
data class FirestoreTimestamp(
    @SerialName("_seconds")
    val seconds: Long = 0,
    @SerialName("_nanos")
    val nanoseconds: Long = 0
)
