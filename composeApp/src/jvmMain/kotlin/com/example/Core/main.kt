package com.example.Societas

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.Core.FireBaseConfig.initializeFirebase
import com.example.societas.DependencyInjection.appModule
import com.example.UI.Screens.Home.SocietasHomeScreen
import org.koin.core.context.startKoin

fun main() = application {

    initializeFirebase()

    startKoin {
        modules(appModule)
    }
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "societas",
    ) {
        SocietasHomeScreen()
    }
}