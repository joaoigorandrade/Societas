package com.example.Navigation.commands

import com.example.Navigation.api.Navigator
import com.example.Navigation.api.Route

class ReplaceCommand(private val route: Route) : NavigationCommand {
    override fun execute(navigator: Navigator) {
        navigator.replace(route)
    }
}