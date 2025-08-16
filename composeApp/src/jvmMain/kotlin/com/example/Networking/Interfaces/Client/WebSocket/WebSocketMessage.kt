package com.example.Networking.Interfaces.Client.WebSocket

import kotlinx.serialization.Serializable

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