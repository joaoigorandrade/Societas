package com.example.UI.Screens.Auth

sealed interface AuthViewState {
    object Idle : AuthViewState
    object Loading : AuthViewState
    object Success : AuthViewState
    data class Error(val message: String) : AuthViewState
}
