package com.example.Domain.Repository.Firebase

import com.example.Networking.Core.NetworkResult
import com.example.UI.Components.ChatMessage.SocietasChatModel
import kotlinx.coroutines.flow.Flow

interface FirestoreRepositoryInterface {
    fun getMessages(userId: String, agentId: String): Flow<NetworkResult<SocietasChatModel>>
}
