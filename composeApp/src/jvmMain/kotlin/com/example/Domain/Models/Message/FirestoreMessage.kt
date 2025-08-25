package com.example.Domain.Models.Message

data class FirestoreMessage(
    val content: String = "",
    val sender: String = "",
    val time: Any? = null,
    val status: String? = null
)
