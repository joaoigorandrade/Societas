package com.example.Networking.Interceptors

import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.Interceptors.LoggingInterceptor
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.EmptyContent
import io.ktor.http.content.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoggingInterceptorImpl(
    private val logger: NetworkLogger = ConsoleNetworkLogger(),
    private val logLevel: LogLevel = LogLevel.INFO
) : LoggingInterceptor {
    
    override val priority: Int = 50
    
    override suspend fun interceptRequest(request: HttpRequestBuilder): HttpRequestBuilder {
        if (logLevel >= LogLevel.INFO) {
            logRequest(request)
        }
        return request
    }
    
    override suspend fun interceptResponse(response: HttpResponse): HttpResponse {
        if (logLevel >= LogLevel.INFO) {
            logResponse(response)
        }
        return response
    }
    
    override suspend fun <T> interceptError(error: NetworkResult.Error): NetworkResult<T> {
        if (logLevel >= LogLevel.ERROR) {
            logError(Exception(error.message))
        }
        return error
    }
    
    override suspend fun logRequest(request: HttpRequestBuilder) {
        withContext(Dispatchers.IO) {
            val logMessage = buildString {
                appendLine("🚀 HTTP Request")
                appendLine("Method: ${request.method.value}")
                appendLine("URL: ${request.url}")
                
                if (!request.headers.build().isEmpty()) {
                    appendLine("Headers:")
                    request.headers.build().forEach { name, values ->
                        values.forEach { value ->
                            appendLine("  $name: ${maskSensitiveHeader(name, value)}")
                        }
                    }
                }
                
                when (val content = request.body) {
                    is TextContent -> {
                        appendLine("Body:")
                        appendLine(content.text)
                    }
                    is ByteArrayContent -> {
                        appendLine("Body: Binary data (${content.bytes().size} bytes)")
                    }
                    else -> {
                        if (content !is EmptyContent) {
                            appendLine("Body: ${content::class.simpleName}")
                        }
                    }
                }
            }
            logger.log(LogLevel.INFO, logMessage)
        }
    }
    
    override suspend fun logResponse(response: HttpResponse) {
        withContext(Dispatchers.IO) {
            val logMessage = buildString {
                appendLine("📥 HTTP Response")
                appendLine("Status: ${response.status.value} ${response.status.description}")
                appendLine("URL: ${response.request.url}")
                
                if (!response.headers.isEmpty()) {
                    appendLine("Headers:")
                    response.headers.forEach { name, values ->
                        values.forEach { value ->
                            appendLine("  $name: $value")
                        }
                    }
                }
                
                appendLine("Content-Type: ${response.headers["Content-Type"]}")
                appendLine("Content-Length: ${response.headers["Content-Length"]}")
            }
            logger.log(LogLevel.INFO, logMessage)
        }
    }
    
    override suspend fun logError(error: Exception) {
        withContext(Dispatchers.IO) {
            val logMessage = buildString {
                appendLine("❌ HTTP Error")
                appendLine("Error: ${error::class.simpleName}")
                appendLine("Message: ${error.message}")
                if (logLevel >= LogLevel.DEBUG) {
                    appendLine("Stack trace:")
                    appendLine(error.stackTraceToString())
                }
            }
            logger.log(LogLevel.ERROR, logMessage)
        }
    }
    
    private fun maskSensitiveHeader(name: String, value: String): String {
        return when (name.lowercase()) {
            "authorization" -> "Bearer ***"
            "cookie" -> "***"
            "set-cookie" -> "***"
            else -> value
        }
    }
}

enum class LogLevel {
    NONE, ERROR, WARN, INFO, DEBUG;
}

interface NetworkLogger {
    fun log(level: LogLevel, message: String)
}

class ConsoleNetworkLogger : NetworkLogger {
    override fun log(level: LogLevel, message: String) {
        val prefix = when (level) {
            LogLevel.ERROR -> "[ERROR]"
            LogLevel.WARN -> "[WARN]"
            LogLevel.INFO -> "[INFO]"
            LogLevel.DEBUG -> "[DEBUG]"
            LogLevel.NONE -> ""
        }
        println("$prefix $message")
    }
}
