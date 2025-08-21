package com.example.Navigation.utils

import com.example.Navigation.api.Route

object DeepLinkParser {
    fun parse(uri: String): Route? {
        // Simple parser, should be replaced with a more robust implementation
        if (uri.startsWith("app://societas/")) {
            val path = uri.substring("app://societas/".length)
            return Route(path)
        }
        return null
    }
}