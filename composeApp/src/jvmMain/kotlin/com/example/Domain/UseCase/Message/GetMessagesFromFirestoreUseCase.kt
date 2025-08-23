package com.example.Domain.UseCase.Message

import com.example.Domain.Repository.Firebase.FirestoreRepositoryInterface
import com.example.Networking.Core.NetworkResult
import com.example.UI.Components.ChatMessage.SocietasChatModel

class GetMessagesFromFirestoreUseCase(private val firestoreRepository: FirestoreRepositoryInterface) {

    suspend fun execute(userId: String, agentId: String): NetworkResult<SocietasChatModel> {
        return firestoreRepository.getMessages(userId, agentId)
    }
}
