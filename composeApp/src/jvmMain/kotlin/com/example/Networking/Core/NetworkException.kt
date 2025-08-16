package com.example.Networking.Core

import kotlinx.serialization.Serializable

@Serializable
sealed class NetworkException(override val message: String) : Exception(message) {

    data object NetworkUnavailable : NetworkException("Network is unavailable")

    data class HttpError(val code: Int, override val message: String) : NetworkException(message)
    data class SerializationError(override val message: String) : NetworkException(message)
    data class TimeoutError(override val message: String) : NetworkException(message)
    data class UnknownError(override val message: String) : NetworkException(message)
}