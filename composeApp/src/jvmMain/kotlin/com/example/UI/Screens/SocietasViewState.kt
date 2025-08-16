package com.example.UI.Screens
sealed interface SocietasViewState {
    data object Loading: SocietasViewState
    data class Success(val data: SocietasScreenModel): SocietasViewState
    data class Error(val message: String): SocietasViewState
}