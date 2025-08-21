package com.example.Navigation.commands

import com.example.Navigation.api.Navigator

class BackCommand : NavigationCommand {
    override fun execute(navigator: Navigator) {
        navigator.navigateBack()
    }
}