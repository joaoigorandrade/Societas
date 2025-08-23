package com.example.societas.di

import com.example.Domain.Repository.Rest.ApiRepository
import com.example.Domain.Repository.Rest.ApiRepositoryInterface
import com.example.Domain.UseCase.Home.SocietasHomeUseCase
import com.example.Domain.UseCase.Message.GetMessagesUseCase
import com.example.Domain.UseCase.Message.SendMessageUseCase
import com.example.Domain.UseCase.Message.GetMessagesFromFirestoreUseCase
import org.koin.dsl.module

val domainModule = module {
    single {
        SocietasHomeUseCase(get())
    }
    factory { SendMessageUseCase(get()) }
    factory { GetMessagesUseCase(get()) }
    factory { GetMessagesFromFirestoreUseCase(get()) }

    single<ApiRepositoryInterface> {
        ApiRepository(get())
    }
}
