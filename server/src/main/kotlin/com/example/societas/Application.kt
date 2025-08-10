package com.example.societas

import com.example.societas.Configurations.DatabaseFactory
import com.example.societas.Configurations.appModule
import com.example.societas.Configurations.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

const val SERVER_PORT = 8080

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    try {
        DatabaseFactory.init()
        environment.log.info("Database initialized successfully")
    } catch (e: Exception) {
        environment.log.error("Database initialization failed: ${e.message}. Continuing without DB.", e)
    }
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
}