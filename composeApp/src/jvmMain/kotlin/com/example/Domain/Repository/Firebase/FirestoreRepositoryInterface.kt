package com.example.Domain.Repository.Firebase

import com.example.Networking.Core.NetworkResult
import com.example.UI.Components.ChatMessage.SocietasChatModel

interface FirestoreRepositoryInterface {
    suspend fun getMessages(userId: String, agentId: String): NetworkResult<SocietasChatModel>
}