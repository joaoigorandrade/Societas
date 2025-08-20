package com.example.Navigation.core

import com.example.Navigation.api.Navigator
import com.example.Navigation.api.Route

class NavigationRouter(
    val stack: NavigationStack = NavigationStack()
) : Navigator {
    override fun navigateTo(route: Route) {
        stack.push(route)
    }

    override fun navigateBack() {
        stack.pop()
    }

    override fun replace(route: Route) {
        stack.replace(route)
    }
}