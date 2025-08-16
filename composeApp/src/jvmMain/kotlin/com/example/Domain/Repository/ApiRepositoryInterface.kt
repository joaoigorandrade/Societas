package com.example.Domain.Repository

import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.RequestInterface

interface ApiRepositoryInterface {
    suspend fun <T> execute(request: RequestInterface): NetworkResult<T>
}