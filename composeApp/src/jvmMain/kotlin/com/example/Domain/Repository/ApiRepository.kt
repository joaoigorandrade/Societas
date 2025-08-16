package com.example.Domain.Repository

import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.Client.Http.NetworkingOperationInterface

class ApiRepository(
    private val networkingOperationInterface: NetworkingOperationInterface
) : ApiRepositoryInterface {
    
    override suspend fun <T> get(
        endpoint: String,
        queryParams: Map<String, String>
    ): NetworkResult<T> {
        return networkingOperationInterface.get(endpoint, queryParams = queryParams)
    }
    
    override suspend fun <T, R> post(
        endpoint: String,
        body: T
    ): NetworkResult<R> {
        return networkingOperationInterface.post(endpoint, body)
    }
    
    override suspend fun <T, R> put(
        endpoint: String,
        body: T
    ): NetworkResult<R> {
        return networkingOperationInterface.put(endpoint, body)
    }
    
    override suspend fun <T, R> patch(
        endpoint: String,
        body: T
    ): NetworkResult<R> {
        return networkingOperationInterface.patch(endpoint, body)
    }
    
    override suspend fun delete(
        endpoint: String
    ): NetworkResult<Unit> {
        return networkingOperationInterface.delete(endpoint)
    }
}