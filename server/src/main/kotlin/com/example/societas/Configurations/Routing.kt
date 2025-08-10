package com.example.societas.Configurations

import com.example.societas.Routes.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        healthRouting()
        exampleRoutes()
        userRoutes()
        conversationRoutes()
        messageRoutes()
        conversationParticipantRoutes()
    }
}

