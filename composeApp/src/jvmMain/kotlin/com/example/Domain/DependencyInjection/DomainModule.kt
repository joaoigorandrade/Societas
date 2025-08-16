package com.example.societas.di

import com.example.Domain.Repository.ApiRepository
import com.example.Domain.Repository.ApiRepositoryInterface
import com.example.Domain.UseCase.Home.SocietasHomeUseCase
import org.koin.dsl.module

val domainModule = module {
    single {
        SocietasHomeUseCase(get())
    }
    single<ApiRepositoryInterface> {
        ApiRepository(get())
    }
}
