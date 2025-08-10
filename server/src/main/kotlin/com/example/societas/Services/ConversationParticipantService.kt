package com.example.societas.Services

import com.example.societas.Models.ConversationParticipant
import com.example.societas.Models.ConversationParticipants
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class ConversationParticipantService {
    fun getParticipantsByConversationId(conversationId: Long): List<ConversationParticipant> = transaction {
        ConversationParticipants.select { ConversationParticipants.conversationId eq conversationId }
            .map { toConversationParticipant(it) }
    }

    fun addParticipant(participant: ConversationParticipant): ConversationParticipant {
        transaction {
            ConversationParticipants.insert {
                it[userId] = participant.userId
                it[conversationId] = participant.conversationId
                it[joinedAt] = LocalDateTime.now().toString()
            }
        }
        return participant
    }

    fun removeParticipant(userId: Long, conversationId: Long): Boolean = transaction {
        ConversationParticipants.deleteWhere {
            (ConversationParticipants.userId eq userId) and (ConversationParticipants.conversationId eq conversationId)
        } > 0
    }

    private fun toConversationParticipant(row: ResultRow): ConversationParticipant =
        ConversationParticipant(
            userId = row[ConversationParticipants.userId],
            conversationId = row[ConversationParticipants.conversationId],
            joinedAt = row[ConversationParticipants.joinedAt]
        )
}
