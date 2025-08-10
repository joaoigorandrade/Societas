package com.example.societas.Routes

import com.example.societas.Controllers.HealthController
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.healthRouting() {
    val healthController by inject<HealthController>()
    route("/health") {
        get {
            call.respond(healthController.getHealth())
        }
    }
}
