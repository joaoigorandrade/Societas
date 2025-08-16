package com.example.Networking.DependencyInjection

import com.example.Networking.Core.Configurations.NetworkConfiguration
import com.example.Networking.Implementation.NetworkingOperation
import com.example.Networking.Interfaces.Client.Http.NetworkingOperationInterface
import com.example.Networking.Manager.NetworkManagerInterface
import com.example.Networking.Manager.NetworkManager
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    
    single<NetworkConfiguration>(named("development")) {
        NetworkConfiguration.development("http://localhost:3000")
    }
    
    single<NetworkConfiguration>(named("production")) {
        NetworkConfiguration.production("https://api.societas.com")
    }
    
    single<NetworkingOperationInterface> {
        NetworkingOperation(
            config = get<NetworkConfiguration>(named("development")),
        )
    }
    
    single<NetworkManagerInterface> {
        NetworkManager(
            networkingOperationInterface = get()
        )
    }
}