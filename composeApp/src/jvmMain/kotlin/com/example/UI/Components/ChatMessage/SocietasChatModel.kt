package com.example.UI.Components.ChatMessage

import kotlinx.serialization.Serializable

@Serializable
data class SocietasChatModel(
    val chatId: String,
    val messages: List<Message>
) {
    @Serializable
    data class Message(
        val text: String,
        val author: String
    )
}