package com.example.UI.Screens.Auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.Domain.Models.Auth.AuthApiResponse
import com.example.Domain.Models.Auth.AuthRequest
import com.example.Domain.UseCase.Auth.SignInUseCase
import com.example.Domain.UseCase.Auth.SignUpUseCase
import com.example.Navigation.api.Navigator
import com.example.Navigation.api.Route
import com.example.Networking.Core.NetworkException
import com.example.Networking.Core.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

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

    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var confirmPasswordError by mutableStateOf<String?>(null)

    private var hasSubmittedOnce by mutableStateOf(false)

    private val _authState = MutableStateFlow<AuthViewState>(AuthViewState.Idle)
    val authState = _authState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        email = newEmail
        if (hasSubmittedOnce) validateEmail()
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        if (hasSubmittedOnce) validatePassword()
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
        if (hasSubmittedOnce) validateConfirmPassword()
    }

    fun setAuth(mode: AuthMode) {
        authMode = mode
        _authState.value = AuthViewState.Idle
        hasSubmittedOnce = false
        emailError = null
        passwordError = null
        confirmPasswordError = null
    }

    private fun validateEmail(): Boolean {
        emailError = if (!email.contains("@") || email.length < 5) "Invalid email address" else null
        return emailError == null
    }

    private fun validatePassword(): Boolean {
        passwordError = if (password.length < 6) "Password must be at least 6 characters" else null
        return passwordError == null
    }

    private fun validateConfirmPassword(): Boolean {
        confirmPasswordError = if (password != confirmPassword) "Passwords do not match" else null
        return confirmPasswordError == null
    }

    private fun validate(): Boolean {
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()
        val isConfirmPasswordValid = if (authMode == AuthMode.SIGN_UP) validateConfirmPassword() else true
        return isEmailValid && isPasswordValid && isConfirmPasswordValid
    }

    fun signIn() {
        hasSubmittedOnce = true
        if (!validate()) return

        scope.launch {
            _authState.value = AuthViewState.Loading
            val result = signInUseCase.execute(AuthRequest(email, password))
            handleAuthResult(result)
        }
    }

    fun signUp() {
        hasSubmittedOnce = true
        if (!validate()) return

        scope.launch {
            _authState.value = AuthViewState.Loading
            val result = signUpUseCase.execute(AuthRequest(email, password))
            handleAuthResult(result)
        }
    }

    private fun handleAuthResult(result: NetworkResult<AuthApiResponse>) {
        when (result) {
            is NetworkResult.Success -> {
                val apiResponse = result.data
                if (apiResponse.success && apiResponse.data != null) {
                    _authState.value = AuthViewState.Success
                    navigator.replace(Route("home"))
                } else {
                    emailError = apiResponse.error?.email
                    passwordError = apiResponse.error?.password
                    _authState.value = AuthViewState.Error(apiResponse.message ?: "An unknown error occurred")
                }
            }
            is NetworkResult.Error -> {
                val exception = result.exception
                if (exception is NetworkException.HttpError && exception.errorBody != null) {
                    try {
                        val errorResponse = Json.decodeFromString<AuthApiResponse>(exception.errorBody)
                        emailError = errorResponse.error?.email
                        passwordError = errorResponse.error?.password
                        _authState.value = AuthViewState.Error(errorResponse.message ?: result.message)
                    } catch (e: Exception) {
                        _authState.value = AuthViewState.Error(result.message)
                    }
                } else {
                    _authState.value = AuthViewState.Error(result.message)
                }
            }
            is NetworkResult.Loading -> {
                _authState.value = AuthViewState.Loading
            }
        }
    }
}
