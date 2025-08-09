package com.example.Networking.Interfaces

import com.example.Networking.Core.NetworkResult
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

@Serializable
enum class WebSocketState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    RECONNECTING,
    ERROR,
    CLOSED
}

@Serializable
sealed class WebSocketMessage {
    @Serializable
    data class Text(val content: String) : WebSocketMessage()
    
    @Serializable
    data class Binary(val data: ByteArray) : WebSocketMessage() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            
            other as Binary
            
            return data.contentEquals(other.data)
        }
        
        override fun hashCode(): Int {
            return data.contentHashCode()
        }
    }
    
    @Serializable
    data class Json<T>(val data: T) : WebSocketMessage()
}

interface WebSocketEventListener {
    suspend fun onConnected()
    suspend fun onDisconnected()
    suspend fun onError(exception: Exception)
    suspend fun onMessage(message: WebSocketMessage)
}
