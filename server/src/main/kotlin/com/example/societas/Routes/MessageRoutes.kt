package com.example.societas.Routes

import com.example.societas.Controllers.MessageController
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.messageRoutes() {
    val messageController by inject<MessageController>()

    route("/messages") {
        get {
            messageController.getAllMessages(call)
        }
        get("/{id}") {
            messageController.getMessageById(call)
        }
        post {
            messageController.createMessage(call)
        }
        put("/{id}") {
            messageController.updateMessage(call)
        }
        delete("/{id}") {
            messageController.deleteMessage(call)
        }
    }
}
