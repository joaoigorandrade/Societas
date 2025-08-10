package com.example.societas.Services

import com.example.societas.Models.Conversation
import com.example.societas.Models.Conversations
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class ConversationService {
    fun getAllConversations(): List<Conversation> = transaction {
        Conversations.selectAll().map { toConversation(it) }
    }

    fun getConversationById(id: Long): Conversation? = transaction {
        Conversations.select { Conversations.id eq id }
            .map { toConversation(it) }
            .singleOrNull()
    }

    fun createConversation(conversation: Conversation): Conversation {
        val newConversationId = transaction {
            val stmt = Conversations.insert {
                it[name] = conversation.name
                it[isGroupChat] = conversation.isGroupChat
                it[creatorId] = conversation.creatorId
                it[createdAt] = LocalDateTime.now().toString()
                it[updatedAt] = LocalDateTime.now().toString()
            }
            stmt[Conversations.id]
        }
        return getConversationById(newConversationId)!!
    }

    fun updateConversation(id: Long, conversation: Conversation): Conversation? {
        transaction {
            Conversations.update({ Conversations.id eq id }) {
                it[name] = conversation.name
                it[isGroupChat] = conversation.isGroupChat
                it[updatedAt] = LocalDateTime.now().toString()
            }
        }
        return getConversationById(id)
    }

    fun deleteConversation(id: Long): Boolean = transaction {
        Conversations.deleteWhere { Conversations.id eq id } > 0
    }

    private fun toConversation(row: ResultRow): Conversation =
        Conversation(
            id = row[Conversations.id],
            name = row[Conversations.name],
            isGroupChat = row[Conversations.isGroupChat],
            creatorId = row[Conversations.creatorId],
            createdAt = row[Conversations.createdAt],
            updatedAt = row[Conversations.updatedAt]
        )
}
