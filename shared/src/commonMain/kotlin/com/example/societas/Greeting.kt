package com.example.societas

interface GreetingService {
    fun greet(): String
}

class Greeting : GreetingService {
    private val platform = getPlatform()

    override fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}