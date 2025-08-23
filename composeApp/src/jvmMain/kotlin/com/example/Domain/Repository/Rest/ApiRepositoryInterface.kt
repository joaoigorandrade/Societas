package com.example.Domain.Repository.Rest

import com.example.Networking.Interfaces.RequestInterface
import io.ktor.client.statement.HttpResponse

interface ApiRepositoryInterface {
    suspend fun executeRaw(request: RequestInterface): HttpResponse
}