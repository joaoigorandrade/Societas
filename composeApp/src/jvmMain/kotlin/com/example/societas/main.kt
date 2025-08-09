package com.example.Societas

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.UI.App
import com.example.Societas.DependencyInjection.appModule
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(appModule)
    }
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "societas",
    ) {
        App()
    }
}