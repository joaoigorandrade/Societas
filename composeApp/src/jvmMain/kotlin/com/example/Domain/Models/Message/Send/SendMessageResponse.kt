package com.example.Domain.Models.Message.Send
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageResponse(val status: String, val responseMessage: String? = null)
