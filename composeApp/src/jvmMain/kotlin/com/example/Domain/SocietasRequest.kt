package com.example.Networking.Interfaces

import com.example.Networking.RequestParameters
import io.ktor.http.HttpMethod

enum class SocietasRequest : RequestInterface {
    Home {
        override val method: HttpMethod
            get() = HttpMethod.Get
        override val path: String
            get() = "/home"
        override val parameters: RequestParameters
            get() = RequestParameters.None
        override val headers: Map<String, String>?
            get() = null
    }
}
