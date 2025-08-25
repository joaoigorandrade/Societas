package com.example.Domain.UseCase.Message

import com.example.Domain.Repository.Firebase.FirestoreRepositoryInterface
import com.example.Networking.Core.NetworkResult
import com.example.UI.Components.ChatMessage.SocietasChatModel
import kotlinx.coroutines.flow.Flow

class GetMessagesFromFirestoreUseCase(private val firestoreRepository: FirestoreRepositoryInterface) {

    fun execute(userId: String, agentId: String): Flow<NetworkResult<SocietasChatModel>> {
        return firestoreRepository.getMessages(userId, agentId)
    }
}
