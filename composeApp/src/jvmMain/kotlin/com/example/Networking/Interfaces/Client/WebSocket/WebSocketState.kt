package com.example.Networking.Interfaces.Client.WebSocket

import kotlinx.serialization.Serializable

@Serializable
enum class WebSocketState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    RECONNECTING,
    ERROR,
    CLOSED
}