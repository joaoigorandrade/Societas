package com.example.societas

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.societas.di.appModule
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