package com.example.Networking.Interfaces.Interceptors

import com.example.Networking.Interfaces.Interceptors.NetworkInterceptor

interface RetryInterceptor : NetworkInterceptor {
    suspend fun shouldRetry(attempt: Int, error: Exception): Boolean
    suspend fun getRetryDelay(attempt: Int): Long
    val maxRetries: Int
}