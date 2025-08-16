package com.example.societas.di

import SocietasHomeScreenViewModel
import org.koin.dsl.module

val uiModule = module {
    factory { SocietasHomeScreenViewModel(get()) }
}
