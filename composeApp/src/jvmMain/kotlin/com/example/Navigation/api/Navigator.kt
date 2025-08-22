package com.example.Navigation.api

interface Navigator {
    fun navigateTo(route: Route)
    fun navigateBack()
    fun replace(route: Route)
}