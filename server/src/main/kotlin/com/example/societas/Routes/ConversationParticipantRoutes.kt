package com.example.societas.Routes

import com.example.societas.Controllers.ConversationParticipantController
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.conversationParticipantRoutes() {
    val participantController by inject<ConversationParticipantController>()

    route("/conversations/{conversationId}/participants") {
        get {
            participantController.getParticipantsByConversationId(call)
        }
        post {
            participantController.addParticipant(call)
        }
        delete("/{userId}") {
            participantController.removeParticipant(call)
        }
    }
}
