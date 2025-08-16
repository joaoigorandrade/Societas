package com.example.Networking.Interfaces.Client.WebSocket

interface WebSocketEventListener {
    suspend fun onConnected()
    suspend fun onDisconnected()
    suspend fun onError(exception: Exception)
    suspend fun onMessage(message: WebSocketMessage)
}
