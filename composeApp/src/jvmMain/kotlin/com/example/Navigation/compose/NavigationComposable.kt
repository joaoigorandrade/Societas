package com.example.Navigation.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.Navigation.api.Navigator
import com.example.Navigation.api.Route
import com.example.Navigation.core.NavigationStack

val LocalNavigator: ProvidableCompositionLocal<Navigator> = staticCompositionLocalOf { error("No navigator found!") }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationComposable(stack: NavigationStack, content: @Composable (Route) -> Unit) {
    val currentRoute = stack.current()
    if (currentRoute != null) {
        AnimatedContent(targetState = currentRoute) { route ->
            content(route)
        }
    }
}