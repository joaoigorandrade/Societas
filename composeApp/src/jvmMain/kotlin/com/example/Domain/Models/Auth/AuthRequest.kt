package com.example.Domain.Models.Auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val email: String,
    val pass: String
)
