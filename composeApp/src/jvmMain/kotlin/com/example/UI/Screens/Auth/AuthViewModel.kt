package com.example.UI.Screens.Auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.Domain.Models.Auth.AuthRequest
import com.example.Domain.Models.Auth.AuthResponse
import com.example.Domain.UseCase.Auth.SignInUseCase
import com.example.Domain.UseCase.Auth.SignUpUseCase
import com.example.Navigation.api.Navigator
import com.example.Navigation.api.Route
import com.example.Networking.Core.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AuthMode {
    SIGN_IN,
    SIGN_UP
}

class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val navigator: Navigator
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var authMode by mutableStateOf(AuthMode.SIGN_IN)

    private val _authState = MutableStateFlow<AuthViewState>(AuthViewState.Idle)
    val authState = _authState.asStateFlow()

    fun setAuthType(mode: AuthMode) {
        authMode = mode
        _authState.value = AuthViewState.Idle
    }

    fun signIn() {
        scope.launch {
            _authState.value = AuthViewState.Loading
            val result = signInUseCase.execute(AuthRequest(email, password))
            handleAuthResult(result)
        }
    }

    fun signUp() {
        if (password != confirmPassword) {
            _authState.value = AuthViewState.Error("Passwords do not match")
            return
        }

        scope.launch {
            _authState.value = AuthViewState.Loading
            val result = signUpUseCase.execute(AuthRequest(email, password))
            handleAuthResult(result)
        }
    }

    private fun handleAuthResult(result: NetworkResult<AuthResponse>) {
        when (result) {
            is NetworkResult.Success -> {
                _authState.value = AuthViewState.Success
                navigator.replace(Route("home"))
            }
            is NetworkResult.Error -> {
                _authState.value = AuthViewState.Error(result.message)
            }
            is NetworkResult.Loading -> {
                _authState.value = AuthViewState.Loading
            }
        }
    }
}
