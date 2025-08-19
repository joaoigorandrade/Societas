package com.example.Domain.Repository

import com.example.Networking.Core.NetworkException
import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.RequestInterface
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.ContentConvertException
import kotlinx.serialization.SerializationException

@Suppress("unused")
suspend inline fun <reified T> ApiRepositoryInterface.execute(request: RequestInterface): NetworkResult<T> {
    return try {
        val response = executeRaw(request)
        val model: T = response.body()
        NetworkResult.Success(model)
    } catch (e: Exception) {
        val mapped = when (e) {
            is SerializationException -> NetworkException.SerializationError(e.message ?: "Serialization error")
            is ContentConvertException -> NetworkException.SerializationError(e.message ?: "Serialization error")
            is ClientRequestException -> NetworkException.HttpError(e.response.status.value, e.message ?: "Client error")
            is ServerResponseException -> NetworkException.HttpError(e.response.status.value, e.message ?: "Server error")
            is HttpRequestTimeoutException -> NetworkException.TimeoutError(e.message ?: "Request timeout")
            else -> NetworkException.UnknownError(e.message ?: "Unknown error occurred")
        }
        NetworkResult.Error(mapped)
    }
}
