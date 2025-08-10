package com.example.societas.Configurations

import com.example.societas.Controllers.HealthController
import com.example.societas.Services.HealthService
import com.example.societas.Routes.exampleRoutes
import com.example.societas.Routes.healthRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        val healthController = HealthController(HealthService())
        healthRouting(healthController)
        exampleRoutes()
    }
}

