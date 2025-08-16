package com.example.Networking

sealed class RequestParameters {
    data class Query(val params: Map<String, String>) : RequestParameters()
    data class Body(val data: Any) : RequestParameters()
    object None : RequestParameters()
}
