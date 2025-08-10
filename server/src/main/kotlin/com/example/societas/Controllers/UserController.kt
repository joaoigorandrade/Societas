package com.example.societas.Controllers

import com.example.societas.Models.User
import com.example.societas.Services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class UserController(private val userService: UserService) {
    suspend fun getAllUsers(call: ApplicationCall) {
        val users = userService.getAllUsers()
        call.respond(users)
    }

    suspend fun getUserById(call: ApplicationCall) {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return
        }
        val user = userService.getUserById(id)
        if (user != null) {
            call.respond(user)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    suspend fun createUser(call: ApplicationCall) {
        try {
            val user = call.receive<User>()
            val createdUser = userService.createUser(user)
            call.respond(HttpStatusCode.Created, createdUser)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid user data")
        }
    }

    suspend fun updateUser(call: ApplicationCall) {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return
        }
        try {
            val user = call.receive<User>()
            val updatedUser = userService.updateUser(id, user)
            if (updatedUser != null) {
                call.respond(updatedUser)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid user data")
        }
    }

    suspend fun deleteUser(call: ApplicationCall) {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return
        }
        val deleted = userService.deleteUser(id)
        if (deleted) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
