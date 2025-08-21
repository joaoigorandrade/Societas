package com.example.Navigation.di

import com.example.Navigation.api.NavigationService
import com.example.Navigation.core.NavigationRouter
import com.example.Navigation.state.NavigationState
import com.example.Navigation.state.StateFlowManager
import org.koin.dsl.module

val navigationModule = module {
    single { NavigationRouter() }
    single { StateFlowManager(NavigationState.Idle) }
    single<NavigationService> { com.example.Navigation.core.NavigationService(get()) }
}