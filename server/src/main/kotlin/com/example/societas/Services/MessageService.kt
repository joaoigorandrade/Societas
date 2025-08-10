package com.example.societas.Services

import com.example.societas.Models.Message
import com.example.societas.Models.Messages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class MessageService {
    fun getAllMessages(): List<Message> = transaction {
        Messages.selectAll().map { toMessage(it) }
    }

    fun getMessageById(id: Long): Message? = transaction {
        Messages.select { Messages.id eq id }
            .map { toMessage(it) }
            .singleOrNull()
    }

    fun createMessage(message: Message): Message {
        val newMessageId = transaction {
            val stmt = Messages.insert {
                it[conversationId] = message.conversationId
                it[senderId] = message.senderId
                it[content] = message.content
                it[sentAt] = LocalDateTime.now().toString()
            }
            stmt[Messages.id]
        }
        return getMessageById(newMessageId)!!
    }

    fun updateMessage(id: Long, message: Message): Message? {
        transaction {
            Messages.update({ Messages.id eq id }) {
                it[content] = message.content
            }
        }
        return getMessageById(id)
    }

    fun deleteMessage(id: Long): Boolean = transaction {
        Messages.deleteWhere { Messages.id eq id } > 0
    }

    private fun toMessage(row: ResultRow): Message =
        Message(
            id = row[Messages.id],
            conversationId = row[Messages.conversationId],
            senderId = row[Messages.senderId],
            content = row[Messages.content],
            sentAt = row[Messages.sentAt]
        )
}
