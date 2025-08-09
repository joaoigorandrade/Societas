package com.example.Networking.Manager

import com.example.Networking.Interfaces.Client.Http.HttpClient
import com.example.Networking.Interfaces.Client.StreamingHttpClient
import com.example.Networking.Interfaces.Client.WebSocket.WebSocketClient

interface NetworkManager {
    val httpClient: HttpClient
    val streamingClient: StreamingHttpClient
    val webSocketClient: WebSocketClient
    
    suspend fun initialize()
    suspend fun cleanup()
}