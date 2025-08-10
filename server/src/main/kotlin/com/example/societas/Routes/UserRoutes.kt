package com.example.societas.Routes

import com.example.societas.Controllers.UserController
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    val userController by inject<UserController>()

    route("/users") {
        get {
            userController.getAllUsers(call)
        }
        get("/{id}") {
            userController.getUserById(call)
        }
        post {
            userController.createUser(call)
        }
        put("/{id}") {
            userController.updateUser(call)
        }
        delete("/{id}") {
            userController.deleteUser(call)
        }
    }
}
