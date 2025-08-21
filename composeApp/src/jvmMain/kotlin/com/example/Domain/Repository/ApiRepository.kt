package com.example.Domain.Repository

import com.example.Networking.Interfaces.Client.Http.NetworkingOperationInterface
import com.example.Networking.Interfaces.RequestInterface
import io.ktor.client.statement.HttpResponse

class ApiRepository(
    private val networkingOperationInterface: NetworkingOperationInterface
) : ApiRepositoryInterface {

    override suspend fun executeRaw(request: RequestInterface): HttpResponse {
        return networkingOperationInterface.executeRaw(request)
    }
}