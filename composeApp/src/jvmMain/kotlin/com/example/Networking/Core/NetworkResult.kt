package com.example.Networking.Core

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
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

inline fun <T> NetworkResult<T>.onSuccess(action: (T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) action(data)
    return this
}

inline fun <T> NetworkResult<T>.onError(action: (NetworkException) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Error) action(exception)
    return this
}

inline fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.Success(transform(data))
        is NetworkResult.Error -> this
        is NetworkResult.Loading -> this
    }
}
