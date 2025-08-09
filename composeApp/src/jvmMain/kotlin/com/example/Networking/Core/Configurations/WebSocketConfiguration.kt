package com.example.Networking.Core.Configurations

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketConfiguration(
    val url: String,
    val pingInterval: Long = 20_000L,
    val timeout: Long = 30_000L,
    val reconnectAttempts: Int = 5,
    val reconnectDelay: Long = 5_000L,
    val headers: Map<String, String> = emptyMap()
)
