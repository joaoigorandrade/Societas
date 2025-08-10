package com.example.societas.Controllers

import com.example.societas.Models.ConversationParticipant
import com.example.societas.Services.ConversationParticipantService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class ConversationParticipantController(private val participantService: ConversationParticipantService) {
    suspend fun getParticipantsByConversationId(call: ApplicationCall) {
        val conversationId = call.parameters["conversationId"]?.toLongOrNull()
        if (conversationId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid Conversation ID")
            return
        }
        val participants = participantService.getParticipantsByConversationId(conversationId)
        call.respond(participants)
    }

    suspend fun addParticipant(call: ApplicationCall) {
        try {
            val participant = call.receive<ConversationParticipant>()
            val addedParticipant = participantService.addParticipant(participant)
            call.respond(HttpStatusCode.Created, addedParticipant)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid participant data")
        }
    }

    suspend fun removeParticipant(call: ApplicationCall) {
        val userId = call.parameters["userId"]?.toLongOrNull()
        val conversationId = call.parameters["conversationId"]?.toLongOrNull()
        if (userId == null || conversationId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid User ID or Conversation ID")
            return
        }
        val removed = participantService.removeParticipant(userId, conversationId)
        if (removed) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
