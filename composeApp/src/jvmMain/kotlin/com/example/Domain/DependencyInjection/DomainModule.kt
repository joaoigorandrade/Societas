package com.example.societas.di

import com.example.Domain.Repository.ApiRepository
import com.example.Domain.Repository.ApiRepositoryInterface
import com.example.Domain.UseCase.Home.SocietasHomeUseCase
import com.example.Domain.UseCase.Message.GetMessagesUseCase
import com.example.Domain.UseCase.Message.SendMessageUseCase
import org.koin.dsl.module

val domainModule = module {
    single {
        SocietasHomeUseCase(get())
    }
    factory { SendMessageUseCase(get()) }
    factory { GetMessagesUseCase(get()) }

    single<ApiRepositoryInterface> {
        ApiRepository(get())
    }
}
