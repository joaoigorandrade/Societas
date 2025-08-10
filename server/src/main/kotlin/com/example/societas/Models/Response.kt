package com.example.societas.Models

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    val data: T,
    val message: String? = null,
    val success: Boolean = true
)
