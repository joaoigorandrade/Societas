package com.example.UI.Components.ChatMessage
import kotlinx.serialization.Serializable

@Serializable
data class SocietasChatMessageModel(val text: String, val author: String)
