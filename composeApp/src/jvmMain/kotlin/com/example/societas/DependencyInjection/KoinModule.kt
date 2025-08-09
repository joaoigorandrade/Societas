package com.example.societas.di

import com.example.Networking.DependencyInjection.networkModule
import org.koin.dsl.module

val appModule = module {
    includes(
        networkModule,
        uiModule,
        domainModule,
        serviceModule
    )
}
