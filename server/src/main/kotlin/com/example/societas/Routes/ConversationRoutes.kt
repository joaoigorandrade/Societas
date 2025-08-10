package com.example.societas.Routes

import com.example.societas.Controllers.ConversationController
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.conversationRoutes() {
    val conversationController by inject<ConversationController>()

    route("/conversations") {
        get {
            conversationController.getAllConversations(call)
        }
        get("/{id}") {
            conversationController.getConversationById(call)
        }
        post {
            conversationController.createConversation(call)
        }
        put("/{id}") {
            conversationController.updateConversation(call)
        }
        delete("/{id}") {
            conversationController.deleteConversation(call)
        }
    }
}
