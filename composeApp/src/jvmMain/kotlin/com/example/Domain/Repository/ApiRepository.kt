package com.example.Domain.Repository

import com.example.Networking.Core.NetworkResult
import kotlinx.coroutines.flow.Flow

interface ApiRepository {
    suspend fun <T> get(endpoint: String, queryParams: Map<String, String> = emptyMap()): NetworkResult<T>
    suspend fun <T, R> post(endpoint: String, body: T): NetworkResult<R>
    suspend fun <T, R> put(endpoint: String, body: T): NetworkResult<R>
    suspend fun <T, R> patch(endpoint: String, body: T): NetworkResult<R>
    suspend fun delete(endpoint: String): NetworkResult<Unit>
}

interface RealtimeRepository {
    suspend fun connect(): NetworkResult<Unit>
    suspend fun disconnect(): NetworkResult<Unit>
    suspend fun <T> sendMessage(message: T, serializer: kotlinx.serialization.KSerializer<T>): NetworkResult<Unit>
    fun <T> observeMessages(): Flow<NetworkResult<T>>
    fun observeConnectionState(): Flow<Boolean>
}

interface FileRepository {
    suspend fun <T> uploadFile(
        endpoint: String,
        fileData: ByteArray,
        fileName: String,
        mimeType: String,
        onProgress: (Float) -> Unit = {}
    ): NetworkResult<T>
    
    suspend fun downloadFile(
        endpoint: String,
        onProgress: (Float) -> Unit = {}
    ): NetworkResult<ByteArray>
}
