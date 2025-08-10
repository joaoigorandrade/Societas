package com.example.societas.Services

import com.example.societas.Models.User
import com.example.societas.Models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class UserService {
    fun getAllUsers(): List<User> = transaction {
        Users.selectAll().map { toUser(it) }
    }

    fun getUserById(id: Long): User? = transaction {
        Users.select { Users.id eq id }
            .map { toUser(it) }
            .singleOrNull()
    }

    fun createUser(user: User): User {
        val newUserId = transaction {
            val stmt = Users.insert {
                it[username] = user.username
                it[passwordHash] = "hashed_password" // Implement password hashing
                it[displayName] = user.displayName
                it[avatarUrl] = user.avatarUrl
                it[createdAt] = LocalDateTime.now().toString()
                it[updatedAt] = LocalDateTime.now().toString()
            }
            stmt[Users.id]
        }
        return getUserById(newUserId)!!
    }

    fun updateUser(id: Long, user: User): User? {
        transaction {
            Users.update({ Users.id eq id }) {
                it[username] = user.username
                it[displayName] = user.displayName
                it[avatarUrl] = user.avatarUrl
                it[updatedAt] = LocalDateTime.now().toString()
            }
        }
        return getUserById(id)
    }

    fun deleteUser(id: Long): Boolean = transaction {
        Users.deleteWhere { Users.id eq id } > 0
    }

    private fun toUser(row: ResultRow): User =
        User(
            id = row[Users.id],
            username = row[Users.username],
            displayName = row[Users.displayName],
            avatarUrl = row[Users.avatarUrl],
            createdAt = row[Users.createdAt],
            updatedAt = row[Users.updatedAt]
        )
}
