package com.example.Networking.Interfaces.Client.Http

import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.RequestInterface

interface NetworkingOperationInterface {
    suspend fun <T> execute(request: RequestInterface): NetworkResult<T>
}
