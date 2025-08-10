package com.example.societas.Routes

import com.example.societas.Services.ExampleService
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.exampleRoutes() {
    val exampleService = ExampleService()

    get("/example") {
        call.respond(exampleService.getHello())
    }
}
