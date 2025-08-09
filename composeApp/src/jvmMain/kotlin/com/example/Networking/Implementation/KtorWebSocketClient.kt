package com.example.Networking.Implementation

import com.example.Networking.Core.NetworkException
import com.example.Networking.Core.NetworkResult
import com.example.Networking.Core.Configurations.WebSocketConfiguration
import com.example.Networking.Interfaces.WebSocketClient
import com.example.Networking.Interfaces.WebSocketEventListener
import com.example.Networking.Interfaces.WebSocketMessage
import com.example.Networking.Interfaces.WebSocketState
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.HttpMethod
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration.Companion.milliseconds
import io.ktor.client.request.header

class KtorWebSocketClient(
    private val config: WebSocketConfiguration,
    private val eventListener: WebSocketEventListener? = null
) : WebSocketClient {
    
    private val client = HttpClient(CIO) {
        install(WebSockets) {
            pingInterval = config.pingInterval.milliseconds
        }
    }
    
    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        encodeDefaults = false
    }
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val connectionState = MutableStateFlow(WebSocketState.DISCONNECTED)
    private val messageChannel = Channel<WebSocketMessage>(Channel.UNLIMITED)
    private val textMessageChannel = Channel<String>(Channel.UNLIMITED)
    private val binaryMessageChannel = Channel<ByteArray>(Channel.UNLIMITED)
    
    private val _isConnected = AtomicBoolean(false)
    private val sessionRef = AtomicReference<DefaultClientWebSocketSession?>(null)
    private var reconnectJob: Job? = null
    
    override val isConnected: Boolean
        get() = _isConnected.get()
    
    override suspend fun connect(): NetworkResult<Unit> {
        return try {
            if (_isConnected.get()) {
                return NetworkResult.Success(Unit)
            }
            
            connectionState.value = WebSocketState.CONNECTING
            
            val session = client.webSocketSession(
                method = HttpMethod.Get,
                host = extractHost(config.url),
                port = extractPort(config.url),
                path = extractPath(config.url)
            ) {
                config.headers.forEach { (key, value) ->
                    header(key, value)
                }
            }
            
            sessionRef.set(session)
            _isConnected.set(true)
            connectionState.value = WebSocketState.CONNECTED
            
            eventListener?.onConnected()
            
            // Start listening for messages
            startMessageListener(session)
            
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            connectionState.value = WebSocketState.ERROR
            _isConnected.set(false)
            eventListener?.onError(e)
            NetworkResult.Error(NetworkException.UnknownError(e.message ?: "Connection failed"))
        }
    }
    
    override suspend fun disconnect(): NetworkResult<Unit> {
        return try {
            reconnectJob?.cancel()
            sessionRef.get()?.close()
            sessionRef.set(null)
            _isConnected.set(false)
            connectionState.value = WebSocketState.DISCONNECTED
            
            eventListener?.onDisconnected()
            
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            NetworkResult.Error(NetworkException.UnknownError(e.message ?: "Disconnection failed"))
        }
    }
    
    override suspend fun <T> sendMessage(message: T, serializer: kotlinx.serialization.KSerializer<T>): NetworkResult<Unit> {
        return try {
            val session = sessionRef.get() 
                ?: return NetworkResult.Error(NetworkException.NetworkUnavailable)
            
            val jsonMessage = jsonSerializer.encodeToString(serializer, message)
            session.send(Frame.Text(jsonMessage))
            
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            NetworkResult.Error(NetworkException.UnknownError(e.message ?: "Failed to send message"))
        }
    }
    
    override suspend fun sendTextMessage(message: String): NetworkResult<Unit> {
        return try {
            val session = sessionRef.get() 
                ?: return NetworkResult.Error(NetworkException.NetworkUnavailable)
            
            session.send(Frame.Text(message))
            
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            NetworkResult.Error(NetworkException.UnknownError(e.message ?: "Failed to send text message"))
        }
    }
    
    override suspend fun sendBinaryMessage(data: ByteArray): NetworkResult<Unit> {
        return try {
            val session = sessionRef.get() 
                ?: return NetworkResult.Error(NetworkException.NetworkUnavailable)
            
            session.send(Frame.Binary(true, data))
            
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            NetworkResult.Error(NetworkException.UnknownError(e.message ?: "Failed to send binary message"))
        }
    }
    
    override fun <T> observeMessages(): Flow<NetworkResult<T>> {
        return messageChannel.receiveAsFlow()
            .map { message ->
                try {
                    when (message) {
                        is WebSocketMessage.Json<*> -> NetworkResult.Success(message.data as T)
                        else -> NetworkResult.Error(
                            NetworkException.SerializationError("Message is not of expected type")
                        )
                    }
                } catch (e: Exception) {
                    NetworkResult.Error(
                        NetworkException.SerializationError(e.message ?: "Failed to deserialize message")
                    )
                }
            }
    }
    
    override fun observeTextMessages(): Flow<NetworkResult<String>> {
        return textMessageChannel.receiveAsFlow()
            .map { NetworkResult.Success(it) }
    }
    
    override fun observeBinaryMessages(): Flow<NetworkResult<ByteArray>> {
        return binaryMessageChannel.receiveAsFlow()
            .map { NetworkResult.Success(it) }
    }
    
    override fun observeConnectionState(): Flow<WebSocketState> {
        return connectionState.asStateFlow()
    }
    
    /**
     * Starts listening for incoming messages
     */
    private fun startMessageListener(session: DefaultClientWebSocketSession) {
        scope.launch {
            try {
                for (frame in session.incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            val text = frame.readText()
                            textMessageChannel.trySend(text)
                            
                            // Try to parse as JSON for structured messages
                            try {
                                val message = WebSocketMessage.Json(text)
                                messageChannel.trySend(message)
                                eventListener?.onMessage(message)
                            } catch (e: Exception) {
                                val message = WebSocketMessage.Text(text)
                                messageChannel.trySend(message)
                                eventListener?.onMessage(message)
                            }
                        }
                        
                        is Frame.Binary -> {
                            val data = frame.readBytes()
                            binaryMessageChannel.trySend(data)
                            
                            val message = WebSocketMessage.Binary(data)
                            messageChannel.trySend(message)
                            eventListener?.onMessage(message)
                        }
                        
                        is Frame.Close -> {
                            _isConnected.set(false)
                            connectionState.value = WebSocketState.DISCONNECTED
                            eventListener?.onDisconnected()
                            
                            // Attempt reconnection if configured
                            if (config.reconnectAttempts > 0) {
                                startReconnection()
                            }
                        }
                        
                        else -> {
                            // Handle other frame types if needed
                        }
                    }
                }
            } catch (e: Exception) {
                _isConnected.set(false)
                connectionState.value = WebSocketState.ERROR
                eventListener?.onError(e)
                
                // Attempt reconnection on error
                if (config.reconnectAttempts > 0) {
                    startReconnection()
                }
            }
        }
    }
    
    /**
     * Handles automatic reconnection
     */
    private fun startReconnection() {
        reconnectJob?.cancel()
        reconnectJob = scope.launch {
            connectionState.value = WebSocketState.RECONNECTING
            
            repeat(config.reconnectAttempts) { attempt ->
                delay(config.reconnectDelay * (attempt + 1))
                
                val result = connect()
                if (result is NetworkResult.Success) {
                    return@launch
                }
            }
            
            // All reconnection attempts failed
            connectionState.value = WebSocketState.ERROR
        }
    }
    
    /**
     * Utility functions for URL parsing
     */
    private fun extractHost(url: String): String {
        return url.substringAfter("://").substringBefore("/").substringBefore(":")
    }
    
    private fun extractPort(url: String): Int {
        val hostPort = url.substringAfter("://").substringBefore("/")
        return if (hostPort.contains(":")) {
            hostPort.substringAfter(":").toIntOrNull() ?: 80
        } else {
            if (url.startsWith("wss://")) 443 else 80
        }
    }
    
    private fun extractPath(url: String): String {
        val path = url.substringAfter("://").substringAfter("/", "")
        return if (path.isEmpty()) "/" else "/$path"
    }
    
    /**
     * Clean up resources
     */
    fun close() {
        scope.cancel()
        reconnectJob?.cancel()
        client.close()
    }
}
