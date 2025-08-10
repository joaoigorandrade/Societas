package com.example.societas.plugins

import com.example.societas.features.example.exampleRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        exampleRoutes()
    }
}
