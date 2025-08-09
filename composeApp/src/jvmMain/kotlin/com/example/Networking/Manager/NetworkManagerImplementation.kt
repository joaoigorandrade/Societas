package com.example.Networking.Manager

import com.example.Networking.Interfaces.Client.Http.HttpClient
import com.example.Networking.Interfaces.Client.StreamingHttpClient
import com.example.Networking.Interfaces.Client.WebSocket.WebSocketClient
import com.example.Networking.Implementation.KtorHttpClient
import com.example.Networking.Implementation.KtorWebSocketClient

class NetworkManagerImplementation(
    override val httpClient: HttpClient,
    override val streamingClient: StreamingHttpClient,
    override val webSocketClient: WebSocketClient
) : NetworkManager {
    
    override suspend fun initialize() {
    }
    
    override suspend fun cleanup() {
        if (httpClient is KtorHttpClient) {
            httpClient.close()
        }
        if (webSocketClient is KtorWebSocketClient) {
            webSocketClient.close()
        }
    }
}
