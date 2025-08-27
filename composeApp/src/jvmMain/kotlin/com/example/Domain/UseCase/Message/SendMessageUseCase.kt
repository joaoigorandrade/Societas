package com.example.Domain.UseCase.Message

import com.example.Domain.Models.Message.Send.SendMessageBody
import com.example.Domain.Models.Message.Send.SendMessageResponse
import com.example.Domain.Repository.Rest.ApiRepositoryInterface
import com.example.Domain.Repository.execute
import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.SocietasRequest

class SendMessageUseCase(private val repository: ApiRepositoryInterface) {

    suspend fun execute(
        chatId: String,
        agentId: String,
        message: String
    ): NetworkResult<SendMessageResponse> {
        val requestBody = SendMessageBody(
            chatId = chatId,
            agentId = agentId,
            message = message
        )
        val request = SocietasRequest.SendMessage(requestBody)
        return repository.execute(request)
    }
}
