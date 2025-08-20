package com.example.Navigation.api

import androidx.compose.runtime.Composable

class RouteConfig(
    val route: Route,
    val content: @Composable () -> Unit
)