package com.example.Networking.Interfaces

import com.example.Networking.RequestParameters
import io.ktor.http.HttpMethod

import com.example.Domain.Models.Message.Send.SendMessageBody

sealed class SocietasRequest : RequestInterface {
    object Home : SocietasRequest() {
        override val method: HttpMethod
            get() = HttpMethod.Get
        override val path: String
            get() = "/home"
        override val parameters: RequestParameters
            get() = RequestParameters.None
        override val headers: Map<String, String>?
            get() = null
    }

    data class SendMessage(val body: SendMessageBody) : SocietasRequest() {
        override val method: HttpMethod
            get() = HttpMethod.Post
        override val path: String
            get() = "/api/users/${body.userId}/chats/${body.chatId}/messages"
        override val parameters: RequestParameters
            get() = RequestParameters.Body(body)
        override val headers: Map<String, String>?
            get() = null
    }

    data class GetMessages(val userId: String, val agentId: String) : SocietasRequest() {
        override val method: HttpMethod
            get() = HttpMethod.Get
        override val path: String
            get() = "/api/users/$userId/chats/with/$agentId"
        override val parameters: RequestParameters
            get() = RequestParameters.None
        override val headers: Map<String, String>?
            get() = null
    }
}
