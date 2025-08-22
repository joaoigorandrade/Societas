package com.example.Navigation.state

import com.example.Navigation.api.Route

sealed interface NavigationState {
    object Idle : NavigationState
    data class Navigating(val route: Route) : NavigationState
    data class Error(val message: String) : NavigationState
}