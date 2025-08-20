package com.example.Navigation.core

import com.example.Navigation.api.NavigationService
import com.example.Navigation.api.Navigator

class NavigationService(private val navigator: Navigator) : com.example.Navigation.api.NavigationService {
    override fun navigator(): Navigator {
        return navigator
    }
}