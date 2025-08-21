package com.example.Navigation.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.Navigation.api.Route
import com.example.Navigation.api.RouteConfig
import com.example.Navigation.compose.LocalNavigator
import com.example.Navigation.compose.NavigationComposable

class NavigationHost(
    private val navigationRouter: NavigationRouter,
    private val routeConfigs: List<RouteConfig>
) {
    @Composable
    fun build() {
        val initialRoute = routeConfigs.first().route
        navigationRouter.stack.push(initialRoute)

        CompositionLocalProvider(LocalNavigator provides navigationRouter) {
            NavigationComposable(navigationRouter.stack) { route ->
                val config = routeConfigs.find { it.route == route }
                config?.content?.invoke()
            }
        }
    }
}