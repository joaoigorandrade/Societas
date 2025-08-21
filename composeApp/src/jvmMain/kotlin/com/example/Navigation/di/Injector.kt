package com.example.Navigation.di

import org.koin.core.context.startKoin
import org.koin.core.KoinApplication

object Injector {
    fun init() {
        startKoin {
            modules(navigationModule)
        }
    }
}