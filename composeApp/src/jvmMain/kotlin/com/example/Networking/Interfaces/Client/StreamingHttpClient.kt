package com.example.Networking.Interfaces.Client

import com.example.Networking.Core.NetworkResult
import kotlinx.coroutines.flow.Flow

interface StreamingHttpClient {
    fun <T> getStream(
        endpoint: String,
        headers: Map<String, String> = emptyMap()
    ): Flow<NetworkResult<T>>
    
    suspend fun <T> uploadFile(
        endpoint: String,
        fileData: ByteArray,
        fileName: String,
        mimeType: String,
        headers: Map<String, String> = emptyMap(),
        onProgress: (Float) -> Unit = {}
    ): NetworkResult<T>
    
    suspend fun downloadFile(
        endpoint: String,
        headers: Map<String, String> = emptyMap(),
        onProgress: (Float) -> Unit = {}
    ): NetworkResult<ByteArray>
}
