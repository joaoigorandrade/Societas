package com.example.Networking.Core.Configurations

import kotlinx.serialization.Serializable


@Serializable
data class NetworkConfiguration(
    val baseUrl: String,
    val timeout: Long = 30_000L,
    val retryAttempts: Int = 3,
    val retryDelay: Long = 1_000L,
    val enableLogging: Boolean = true,
    val headers: Map<String, String> = emptyMap(),
    val connectTimeout: Long = 10_000L,
    val socketTimeout: Long = 30_000L
) {
    companion object {
        fun default(baseUrl: String): NetworkConfiguration {
            return NetworkConfiguration(baseUrl = baseUrl)
        }
        
        fun development(baseUrl: String): NetworkConfiguration {
            return NetworkConfiguration(
                baseUrl = baseUrl,
                enableLogging = true,
                timeout = 60_000L
            )
        }
        
        fun production(baseUrl: String): NetworkConfiguration {
            return NetworkConfiguration(
                baseUrl = baseUrl,
                enableLogging = false,
                timeout = 15_000L,
                retryAttempts = 5
            )
        }
    }
}