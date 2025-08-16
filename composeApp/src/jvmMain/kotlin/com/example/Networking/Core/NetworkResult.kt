package com.example.Networking.Core

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * Sealed class representing the result of a network operation
 * Follows the Railway programming pattern for handling success/error states
 */
sealed class NetworkResult<out T> {
    @Serializable
    data class Success<T>(val data: T) : NetworkResult<T>()

    @Serializable
    data class Error(
        @Contextual val exception: NetworkException,
        val message: String = exception.message
    ) : NetworkResult<Nothing>()
    
    @Serializable
    data class Loading(val isLoading: Boolean = true) : NetworkResult<Nothing>()
}

/**
 * Custom exception types for different network scenarios
 */
@Serializable
sealed class NetworkException(override val message: String) : Exception(message) {
    data object NetworkUnavailable : NetworkException("Network is unavailable")
    data class HttpError(val code: Int, override val message: String) : NetworkException(message)
    
    data class SerializationError(override val message: String) : NetworkException(message)
    
    data class TimeoutError(override val message: String) : NetworkException(message)
    
    data class UnknownError(override val message: String) : NetworkException(message)
}

/**
 * Extension functions for NetworkResult handling
 */
inline fun <T> NetworkResult<T>.onSuccess(action: (T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) action(data)
    return this
}

inline fun <T> NetworkResult<T>.onError(action: (NetworkException) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Error) action(exception)
    return this
}

inline fun <T> NetworkResult<T>.onLoading(action: (Boolean) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Loading) action(isLoading)
    return this
}

/**
 * Maps the success data to a new type
 */
inline fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.Success(transform(data))
        is NetworkResult.Error -> this
        is NetworkResult.Loading -> this
    }
}
