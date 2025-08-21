package com.example.Navigation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.Navigation.core.NavigationRouter

@Composable
fun NestedNavigator(content: @Composable () -> Unit) {
    val nestedRouter = NavigationRouter()
    CompositionLocalProvider(LocalNavigator provides nestedRouter) {
        content()
    }
}