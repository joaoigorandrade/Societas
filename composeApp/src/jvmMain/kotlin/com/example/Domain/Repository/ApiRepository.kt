package com.example.Domain.Repository

import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.Client.Http.HttpClient
import com.example.Networking.Interfaces.Client.StreamingHttpClient
import com.example.Networking.Interfaces.Client.WebSocket.WebSocketClient
import com.example.Networking.Interfaces.Client.WebSocket.WebSocketState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer

class ApiRepository(
    private val httpClient: HttpClient
) : ApiRepositoryInterface {
    
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

class RealtimeRepositoryImpl(
    private val webSocketClient: WebSocketClient
) : RealtimeRepository {
    
    override suspend fun connect(): NetworkResult<Unit> {
        return webSocketClient.connect()
    }
    
    override suspend fun disconnect(): NetworkResult<Unit> {
        return webSocketClient.disconnect()
    }
    
    override suspend fun <T> sendMessage(message: T, serializer: KSerializer<T>): NetworkResult<Unit> {
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
