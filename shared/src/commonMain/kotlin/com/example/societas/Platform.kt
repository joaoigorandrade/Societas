package com.example.societas

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform