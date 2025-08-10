package com.example.societas.Controllers

import com.example.societas.Models.Message
import com.example.societas.Services.MessageService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class MessageController(private val messageService: MessageService) {
    suspend fun getAllMessages(call: ApplicationCall) {
        val messages = messageService.getAllMessages()
        call.respond(messages)
    }

    suspend fun getMessageById(call: ApplicationCall) {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return
        }
        val message = messageService.getMessageById(id)
        if (message != null) {
            call.respond(message)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    suspend fun createMessage(call: ApplicationCall) {
        try {
            val message = call.receive<Message>()
            val createdMessage = messageService.createMessage(message)
            call.respond(HttpStatusCode.Created, createdMessage)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid message data")
        }
    }

    suspend fun updateMessage(call: ApplicationCall) {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return
        }
        try {
            val message = call.receive<Message>()
            val updatedMessage = messageService.updateMessage(id, message)
            if (updatedMessage != null) {
                call.respond(updatedMessage)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid message data")
        }
    }

    suspend fun deleteMessage(call: ApplicationCall) {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return
        }
        val deleted = messageService.deleteMessage(id)
        if (deleted) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
