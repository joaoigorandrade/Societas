package com.example.societas.Models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class ConversationParticipant(
    val userId: Long,
    val conversationId: Long,
    val joinedAt: String
)

object ConversationParticipants : Table() {
    val userId = long("user_id")
    val conversationId = long("conversation_id")
    val joinedAt = text("joined_at")

    override val primaryKey = PrimaryKey(userId, conversationId)
}
