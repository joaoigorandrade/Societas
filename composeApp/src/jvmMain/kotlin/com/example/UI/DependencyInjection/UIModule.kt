package com.example.societas.di

import SocietasHomeScreenViewModel
import com.example.UI.Screens.Home.Components.ChatPanel.ChatPanelViewModel
import org.koin.dsl.module

val uiModule = module {
    factory { SocietasHomeScreenViewModel(get()) }
    factory { ChatPanelViewModel(get(), get()) }
}
