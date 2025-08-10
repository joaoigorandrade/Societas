package com.example.societas.Configurations

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import java.net.URI

object DatabaseFactory {
    fun init() {
        val envUrl = System.getenv("DATABASE_URL")?.takeIf { it.isNotBlank() }

        val (finalJdbcUrl, username, password) = if (envUrl != null) {
            // Expecting a URI like: postgres://user:pass@host:port/dbname
            val dbUri = try {
                URI(envUrl)
            } catch (e: Exception) {
                throw IllegalStateException("Invalid DATABASE_URL format: '$envUrl'", e)
            }

            val userInfo = dbUri.userInfo
                ?: throw IllegalStateException("DATABASE_URL must include user info (username:password)")
            val creds = userInfo.split(":", limit = 2)
            val user = creds.getOrNull(0) ?: throw IllegalStateException("DATABASE_URL missing username")
            val pass = creds.getOrNull(1) ?: throw IllegalStateException("DATABASE_URL missing password")

            val host = dbUri.host ?: throw IllegalStateException("DATABASE_URL missing host")
            val port = if (dbUri.port == -1) 5432 else dbUri.port
            val path = dbUri.path ?: throw IllegalStateException("DATABASE_URL missing database name path")
            val dbName = if (path.startsWith("/")) path else "/$path"

            val sslMode = System.getenv("DB_SSLMODE")?.takeIf { it.isNotBlank() }
            val baseUrl = "jdbc:postgresql://$host:$port$dbName"
            val finalUrl = if (sslMode != null) "$baseUrl?sslmode=$sslMode" else baseUrl

            Triple(finalUrl, user, pass)
        } else {
            // Fallback to individual env vars or local defaults
            val host = System.getenv("DB_HOST")?.ifBlank { null } ?: "localhost"
            val port = (System.getenv("DB_PORT")?.ifBlank { null } ?: "5432").toInt()
            val dbName = System.getenv("DB_NAME")?.ifBlank { null } ?: "societas"
            val user = System.getenv("DB_USER")?.ifBlank { null } ?: "postgres"
            val pass = System.getenv("DB_PASSWORD")?.ifBlank { null } ?: "postgres"
            val sslMode = System.getenv("DB_SSLMODE")?.takeIf { it.isNotBlank() }
            val baseUrl = "jdbc:postgresql://$host:$port/$dbName"
            val finalUrl = if (sslMode != null) "$baseUrl?sslmode=$sslMode" else baseUrl
            Triple(finalUrl, user, pass)
        }

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = finalJdbcUrl
            this.username = username
            this.password = password
            driverClassName = "org.postgresql.Driver"
            validate()
        }

        val dataSource = HikariDataSource(hikariConfig)
        Database.connect(dataSource)

        val flyway = Flyway.configure().dataSource(dataSource).load()
        flyway.migrate()
    }
}
