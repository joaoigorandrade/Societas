package com.example.societas.di

import com.example.societas.Greeting
import com.example.societas.GreetingService
import org.koin.dsl.module

val serviceModule = module {
    single<GreetingService> { Greeting() }
}