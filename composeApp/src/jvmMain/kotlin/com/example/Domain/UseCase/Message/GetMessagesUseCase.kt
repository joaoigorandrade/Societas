package com.example.Domain.UseCase.Message

import com.example.Domain.Repository.Rest.ApiRepositoryInterface
import com.example.Domain.Repository.execute
import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.SocietasRequest
import com.example.UI.Components.ChatMessage.SocietasChatModel

class GetMessagesUseCase(private val repository: ApiRepositoryInterface) {

    suspend fun execute(agentId: String): NetworkResult<SocietasChatModel> {
        val request = SocietasRequest.GetMessages(agentId)
        return repository.execute(request)
    }
}
