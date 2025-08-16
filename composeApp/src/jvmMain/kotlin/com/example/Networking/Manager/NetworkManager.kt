package com.example.Networking.Manager

import com.example.Networking.Interfaces.Client.Http.NetworkingOperationInterface
import com.example.Networking.Implementation.NetworkingOperation

class NetworkManager(
    override val networkingOperationInterface: NetworkingOperationInterface,
) : NetworkManagerInterface {
    
    override suspend fun initialize() {
    }
    
    override suspend fun cleanup() {
        if (networkingOperationInterface is NetworkingOperation) {
            networkingOperationInterface.close()
        }
    }
}
