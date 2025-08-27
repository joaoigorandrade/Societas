package com.example.Domain.Models.Message.Send

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageBody(
    val chatId: String,
    val agentId: String,
    val message: String
)
