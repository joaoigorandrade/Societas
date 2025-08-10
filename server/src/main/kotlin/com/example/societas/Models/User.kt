package com.example.societas.Models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import java.time.LocalDateTime

@Serializable
data class User(
    val id: Long,
    val username: String,
    val displayName: String,
    val avatarUrl: String?,
    val createdAt: String,
    val updatedAt: String
)

object Users : Table() {
    val id = long("id").autoIncrement()
    val username = varchar("username", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val displayName = varchar("display_name", 255)
    val avatarUrl = text("avatar_url").nullable()
    val createdAt = text("created_at")
    val updatedAt = text("updated_at")

    override val primaryKey = PrimaryKey(id)
}
