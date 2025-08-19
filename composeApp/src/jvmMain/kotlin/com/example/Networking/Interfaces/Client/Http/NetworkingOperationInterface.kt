package com.example.Networking.Interfaces.Client.Http

import com.example.Networking.Interfaces.RequestInterface
import io.ktor.client.statement.HttpResponse

interface NetworkingOperationInterface {
    suspend fun executeRaw(request: RequestInterface): HttpResponse
}
