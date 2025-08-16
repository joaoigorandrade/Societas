package com.example.societas.di

import com.example.Domain.UseCase.Home.SocietasHomeUseCase
import org.koin.dsl.module

val domainModule = module {
    single {
        SocietasHomeUseCase()
    }
}
