package com.example.Networking.Manager

import com.example.Networking.Interfaces.Client.Http.NetworkingOperationInterface

interface NetworkManagerInterface {
    val networkingOperationInterface: NetworkingOperationInterface

    suspend fun initialize()
    suspend fun cleanup()
}