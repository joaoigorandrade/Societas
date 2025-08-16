package com.example.Networking.Interfaces

import com.example.Networking.RequestParameters
import io.ktor.http.HttpMethod

interface RequestInterface {
    val method: HttpMethod
    val path: String
    val parameters: RequestParameters
    val headers: Map<String, String>?
}
