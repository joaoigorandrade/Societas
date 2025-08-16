package com.example.Societas.DependencyInjection

import com.example.Networking.DependencyInjection.networkModule
import com.example.societas.di.domainModule
import com.example.societas.di.serviceModule
import com.example.societas.di.uiModule
import org.koin.dsl.module

val appModule = module {
    includes(
        networkModule,
        uiModule,
        domainModule,
        serviceModule
    )
}
