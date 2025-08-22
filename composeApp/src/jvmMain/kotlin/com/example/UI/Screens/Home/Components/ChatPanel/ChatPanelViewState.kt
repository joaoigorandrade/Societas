package com.example.UI.Screens.Home.Components.ChatPanel

sealed interface ChatPanelViewState {
    data object Idle : ChatPanelViewState
    data object Loading : ChatPanelViewState
    data object Success : ChatPanelViewState
    data class Error(val message: String) : ChatPanelViewState
}