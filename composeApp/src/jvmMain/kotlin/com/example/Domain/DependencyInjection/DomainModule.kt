package com.example.societas.di

import com.example.Domain.Repository.*
import org.koin.dsl.module

val domainModule = module {
    
    // Repository implementations
    single<ApiRepository> {
        ApiRepositoryImpl(httpClient = get())
    }
    
    single<RealtimeRepository> {
        RealtimeRepositoryImpl(webSocketClient = get())
    }
    
    single<FileRepository> {
        FileRepositoryImpl(streamingClient = get())
    }
}
