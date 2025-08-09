package com.example.Networking.Interfaces.Client.WebSocket

import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.Client.WebSocket.WebSocketMessage
import com.example.Networking.Interfaces.Client.WebSocket.WebSocketState
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

interface WebSocketClient {
    suspend fun connect(): NetworkResult<Unit>
    
    suspend fun disconnect(): NetworkResult<Unit>
    
    suspend fun <T> sendMessage(message: T, serializer: KSerializer<T>): NetworkResult<Unit>
    
    suspend fun sendTextMessage(message: String): NetworkResult<Unit>
    
    suspend fun sendBinaryMessage(data: ByteArray): NetworkResult<Unit>
    
    fun <T> observeMessages(): Flow<NetworkResult<T>>
    
    fun observeTextMessages(): Flow<NetworkResult<String>>
    
    fun observeBinaryMessages(): Flow<NetworkResult<ByteArray>>
    
    fun observeConnectionState(): Flow<WebSocketState>
    
    val isConnected: Boolean
}