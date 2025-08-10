package com.example.societas.Routes

import com.example.societas.Controllers.HealthController
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.healthRouting(healthController: HealthController) {
    route("/health") {
        get {
            call.respond(healthController.getHealth())
        }
    }
}
