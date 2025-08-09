package com.example.Networking.Interceptors.Logging

interface NetworkLogger {
    fun log(level: LogLevel, message: String)
}
