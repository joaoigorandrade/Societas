package com.example.societas.features.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.exampleRoutes() {
    val service = ExampleService()

    route("/examples") {
        get {
            call.respond(service.getExamples())
        }
    }
}
