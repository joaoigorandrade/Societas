package com.example.Navigation.commands

import com.example.Navigation.api.Navigator

interface NavigationCommand {
    fun execute(navigator: Navigator)
}