package com.example.societas.Models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Conversation(
    val id: Long,
    val name: String?,
    val isGroupChat: Boolean,
    val creatorId: Long?,
    val createdAt: String,
    val updatedAt: String
)

object Conversations : Table() {
    val id = long("id").autoIncrement()
    val name = varchar("name", 255).nullable()
    val isGroupChat = bool("is_group_chat").default(false)
    val creatorId = long("creator_id").nullable()
    val createdAt = text("created_at")
    val updatedAt = text("updated_at")

    override val primaryKey = PrimaryKey(id)
}
