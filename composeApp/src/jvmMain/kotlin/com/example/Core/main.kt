package com.example.Societas

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.Core.FireBaseConfig.initializeFirebase
import com.example.Navigation.api.Route
import com.example.Navigation.api.RouteConfig
import com.example.Navigation.core.NavigationHost
import com.example.Navigation.core.NavigationRouter
import com.example.UI.Screens.Auth.AuthScreen
import com.example.societas.DependencyInjection.appModule
import com.example.UI.Screens.Home.SocietasHomeScreen
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

fun main() = application {

    initializeFirebase()

    startKoin {
        modules(appModule)
    }

    val koin = getKoin()
    val navigationRouter: NavigationRouter = koin.get()

    val routeConfigs = listOf(
        RouteConfig(Route("auth")) { AuthScreen() },
        RouteConfig(Route("home")) { SocietasHomeScreen() }
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "societas",
    ) {
        val navigationHost = NavigationHost(navigationRouter, routeConfigs)
        navigationHost.build()
    }
}
