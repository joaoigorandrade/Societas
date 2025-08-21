package com.example.Navigation.utils

import kotlinx.serialization.json.Json

object SerializationUtils {
    val json = Json { encodeDefaults = true }
}