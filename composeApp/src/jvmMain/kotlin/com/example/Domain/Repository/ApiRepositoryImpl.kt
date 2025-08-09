package com.example.Domain.Repository

import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.HttpClient
import com.example.Networking.Interfaces.StreamingHttpClient
import com.example.Networking.Interfaces.WebSocketClient
import com.example.Networking.Interfaces.WebSocketState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ApiRepositoryImpl(
    private val httpClient: HttpClient
) : ApiRepository {
    
    override suspend fun <T> get(
        endpoint: String,
        queryParams: Map<String, String>
    ): NetworkResult<T> {
        return httpClient.get(endpoint, queryParams = queryParams)
    }
    
    override suspend fun <T, R> post(endpoint: String, body: T): NetworkResult<R> {
        return httpClient.post(endpoint, body)
    }
    
    override suspend fun <T, R> put(endpoint: String, body: T): NetworkResult<R> {
        return httpClient.put(endpoint, body)
    }
    
    override suspend fun <T, R> patch(endpoint: String, body: T): NetworkResult<R> {
        return httpClient.patch(endpoint, body)
    }
    
    override suspend fun delete(endpoint: String): NetworkResult<Unit> {
        return httpClient.delete(endpoint)
    }
}

/**
 * Implementation of RealtimeRepository using WebSocket client
 */
class RealtimeRepositoryImpl(
    private val webSocketClient: WebSocketClient
) : RealtimeRepository {
    
    override suspend fun connect(): NetworkResult<Unit> {
        return webSocketClient.connect()
    }
    
    override suspend fun disconnect(): NetworkResult<Unit> {
        return webSocketClient.disconnect()
    }
    
    override suspend fun <T> sendMessage(message: T, serializer: kotlinx.serialization.KSerializer<T>): NetworkResult<Unit> {
        return webSocketClient.sendMessage(message, serializer)
    }
    
    override fun <T> observeMessages(): Flow<NetworkResult<T>> {
        return webSocketClient.observeMessages()
    }
    
    override fun observeConnectionState(): Flow<Boolean> {
        return webSocketClient.observeConnectionState()
            .map { state -> state == WebSocketState.CONNECTED }
    }
}

/**
 * Implementation of FileRepository using streaming HTTP client
 */
class FileRepositoryImpl(
    private val streamingClient: StreamingHttpClient
) : FileRepository {
    
    override suspend fun <T> uploadFile(
        endpoint: String,
        fileData: ByteArray,
        fileName: String,
        mimeType: String,
        onProgress: (Float) -> Unit
    ): NetworkResult<T> {
        return streamingClient.uploadFile(
            endpoint = endpoint,
            fileData = fileData,
            fileName = fileName,
            mimeType = mimeType,
            onProgress = onProgress
        )
    }
    
    override suspend fun downloadFile(
        endpoint: String,
        onProgress: (Float) -> Unit
    ): NetworkResult<ByteArray> {
        return streamingClient.downloadFile(
            endpoint = endpoint,
            onProgress = onProgress
        )
    }
}
