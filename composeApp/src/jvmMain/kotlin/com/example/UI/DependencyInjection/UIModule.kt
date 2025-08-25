package com.example.societas.di

import com.example.UI.Screens.Auth.AuthViewModel
import com.example.UI.Screens.Home.SocietasHomeScreenViewModel
import com.example.UI.Screens.Home.Components.ChatPanel.ChatPanelViewModel
import org.koin.dsl.module

val uiModule = module {
    factory { SocietasHomeScreenViewModel(get()) }
    factory { ChatPanelViewModel(get(), get()) }
    factory { AuthViewModel(get(), get(), get()) }
}
