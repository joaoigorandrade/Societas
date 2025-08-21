package com.example.UI.Screens.Home

sealed interface MessageViewState {
    data object Idle : MessageViewState
    data object Loading : MessageViewState
    data object Success : MessageViewState
    data class Error(val message: String) : MessageViewState
}
