package com.example.societas.Models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Message(
    val id: Long,
    val conversationId: Long,
    val senderId: Long?,
    val content: String,
    val sentAt: String
)

object Messages : Table() {
    val id = long("id").autoIncrement()
    val conversationId = long("conversation_id")
    val senderId = long("sender_id").nullable()
    val content = text("content")
    val sentAt = text("sent_at")

    override val primaryKey = PrimaryKey(id)
}
