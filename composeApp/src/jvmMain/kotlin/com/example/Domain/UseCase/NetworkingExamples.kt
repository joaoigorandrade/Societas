package com.example.Domain.UseCase

import com.example.Domain.Repository.ApiRepository
import com.example.Domain.Repository.FileRepository
import com.example.Domain.Repository.RealtimeRepository
import com.example.Networking.Core.NetworkResult
import com.example.Networking.Core.onError
import com.example.Networking.Core.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable

/**
 * Example use cases demonstrating the networking layer usage
 * These show how to integrate the networking layer with your domain logic
 */

/**
 * Example data models
 */
@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String
)

@Serializable
data class CreateUserRequest(
    val name: String,
    val email: String
)

@Serializable
data class ChatMessage(
    val id: String,
    val userId: String,
    val message: String,
    val timestamp: Long
)

/**
 * User management use case
 */
class UserUseCase(
    private val apiRepository: ApiRepository
) {
    
    suspend fun getUser(userId: String): NetworkResult<User> {
        return apiRepository.get("/users/$userId")
    }
    
    suspend fun getAllUsers(): NetworkResult<List<User>> {
        return apiRepository.get("/users")
    }
    
    suspend fun createUser(request: CreateUserRequest): NetworkResult<User> {
        return apiRepository.post("/users", request)
    }
    
    suspend fun updateUser(userId: String, user: User): NetworkResult<User> {
        return apiRepository.put("/users/$userId", user)
    }
    
    suspend fun deleteUser(userId: String): NetworkResult<Unit> {
        return apiRepository.delete("/users/$userId")
    }
    
    suspend fun searchUsers(query: String): NetworkResult<List<User>> {
        return apiRepository.get("/users/search", mapOf("q" to query))
    }
}

/**
 * Real-time chat use case
 */
class ChatUseCase(
    private val realtimeRepository: RealtimeRepository
) {
    
    suspend fun connectToChat(): NetworkResult<Unit> {
        return realtimeRepository.connect()
    }
    
    suspend fun disconnectFromChat(): NetworkResult<Unit> {
        return realtimeRepository.disconnect()
    }
    
    suspend fun sendMessage(message: ChatMessage): NetworkResult<Unit> {
        return realtimeRepository.sendMessage(message, ChatMessage.serializer())
    }
    
    fun observeMessages(): Flow<NetworkResult<ChatMessage>> {
        return realtimeRepository.observeMessages()
    }
    
    fun observeConnectionStatus(): Flow<Boolean> {
        return realtimeRepository.observeConnectionState()
    }
    
    /**
     * Example of handling real-time messages
     */
    suspend fun startListeningToMessages(
        onMessage: (ChatMessage) -> Unit,
        onError: (String) -> Unit,
        onConnectionChange: (Boolean) -> Unit
    ) {
        // Observe connection status
        observeConnectionStatus()
            .onEach { isConnected ->
                onConnectionChange(isConnected)
            }
        
        // Observe incoming messages
        observeMessages()
            .onEach { result ->
                result
                    .onSuccess { print("succes") }
                    .onError { print("error") }
            }
            .collect()
    }
}

/**
 * File management use case
 */
class FileUseCase(
    private val fileRepository: FileRepository
) {
    
    suspend fun uploadProfilePicture(
        userId: String,
        imageData: ByteArray,
        fileName: String,
        onProgress: (Float) -> Unit = {}
    ): NetworkResult<String> {
        return fileRepository.uploadFile(
            endpoint = "/users/$userId/profile-picture",
            fileData = imageData,
            fileName = fileName,
            mimeType = "image/jpeg",
            onProgress = onProgress
        )
    }
    
    suspend fun downloadFile(
        fileId: String,
        onProgress: (Float) -> Unit = {}
    ): NetworkResult<ByteArray> {
        return fileRepository.downloadFile(
            endpoint = "/files/$fileId/download",
            onProgress = onProgress
        )
    }
}

/**
 * Example of a comprehensive service that uses all networking capabilities
 */
class SocietasService(
    private val userUseCase: UserUseCase,
    private val chatUseCase: ChatUseCase,
    private val fileUseCase: FileUseCase
) {
    
    /**
     * Initialize the service
     */
    suspend fun initialize(): NetworkResult<Unit> {
        // Connect to real-time services
        return chatUseCase.connectToChat()
    }
    
    /**
     * Cleanup resources
     */
    suspend fun cleanup(): NetworkResult<Unit> {
        return chatUseCase.disconnectFromChat()
    }
    
    /**
     * Example of a complex operation combining multiple network calls
     */
    suspend fun createUserAndJoinChat(
        request: CreateUserRequest
    ): NetworkResult<User> {
        // Create user
        val userResult = userUseCase.createUser(request)
        
        return when (userResult) {
            is NetworkResult.Success -> {
                val user = userResult.data
                
                // Connect to chat after user creation
                val chatResult = chatUseCase.connectToChat()
                
                when (chatResult) {
                    is NetworkResult.Success -> NetworkResult.Success(user)
                    is NetworkResult.Error -> chatResult
                    is NetworkResult.Loading -> chatResult
                }
            }
            is NetworkResult.Error -> userResult
            is NetworkResult.Loading -> userResult
        }
    }
}
