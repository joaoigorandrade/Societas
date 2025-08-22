package com.example.Navigation.guards

import com.example.Navigation.api.Route

interface NavigationGuard {
    fun canNavigate(route: Route): Boolean
}