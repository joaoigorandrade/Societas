package com.example.Domain.DependencyInjection

import com.example.Domain.UseCase.Auth.SignInUseCase
import com.example.Domain.UseCase.Auth.SignUpUseCase
import org.koin.dsl.module

val authModule = module {
    factory { SignInUseCase(get()) }
    factory { SignUpUseCase(get()) }
}
