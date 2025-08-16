package com.example.Networking.Interceptors.Logging

import com.example.Networking.Interceptors.Logging.NetworkLogger
import com.example.Networking.Interceptors.Logging.LogLevel

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
