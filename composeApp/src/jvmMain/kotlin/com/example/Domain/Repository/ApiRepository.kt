package com.example.Domain.Repository

import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.Client.Http.NetworkingOperationInterface
import com.example.Networking.Interfaces.RequestInterface

class ApiRepository(
    private val networkingOperationInterface: NetworkingOperationInterface
) : ApiRepositoryInterface {

    override suspend fun <T> execute(request: RequestInterface): NetworkResult<T> {
        return networkingOperationInterface.execute(request)
    }
}