package com.example.Navigation.commands

import com.example.Navigation.api.Navigator
import com.example.Navigation.api.Route

class ForwardCommand(private val route: Route) : NavigationCommand {
    override fun execute(navigator: Navigator) {
        navigator.navigateTo(route)
    }
}