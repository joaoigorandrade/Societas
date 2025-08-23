package com.example.societas.DependencyInjection

import com.example.Networking.DependencyInjection.networkModule
import com.example.societas.di.domainModule
import com.example.societas.di.uiModule
import com.example.societas.di.firestoreModule
import org.koin.dsl.module

val appModule = module {
    includes(
        networkModule,
        uiModule,
        domainModule,
        firestoreModule
    )
}
