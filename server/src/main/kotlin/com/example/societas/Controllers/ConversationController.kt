package com.example.societas.Controllers

import com.example.societas.Models.Conversation
import com.example.societas.Services.ConversationService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class ConversationController(private val conversationService: ConversationService) {
    suspend fun getAllConversations(call: ApplicationCall) {
        val conversations = conversationService.getAllConversations()
        call.respond(conversations)
    }

    suspend fun getConversationById(call: ApplicationCall) {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return
        }
        val conversation = conversationService.getConversationById(id)
        if (conversation != null) {
            call.respond(conversation)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    suspend fun createConversation(call: ApplicationCall) {
        try {
            val conversation = call.receive<Conversation>()
            val createdConversation = conversationService.createConversation(conversation)
            call.respond(HttpStatusCode.Created, createdConversation)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid conversation data")
        }
    }

    suspend fun updateConversation(call: ApplicationCall) {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return
        }
        try {
            val conversation = call.receive<Conversation>()
            val updatedConversation = conversationService.updateConversation(id, conversation)
            if (updatedConversation != null) {
                call.respond(updatedConversation)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid conversation data")
        }
    }

    suspend fun deleteConversation(call: ApplicationCall) {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return
        }
        val deleted = conversationService.deleteConversation(id)
        if (deleted) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
