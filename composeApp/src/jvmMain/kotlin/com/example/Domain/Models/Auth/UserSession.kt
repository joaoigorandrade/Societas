package com.example.Domain.Models.Auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserSessionData(
    val userId: String,
    val token: String
)

object UserSession {
    private val _session = MutableStateFlow<UserSessionData?>(null)
    val session: StateFlow<UserSessionData?> = _session.asStateFlow()

    fun startSession(sessionData: UserSessionData) {
        _session.value = sessionData
    }

    fun endSession() {
        _session.value = null
    }

    fun getToken(): String? = _session.value?.token
    fun getUserId(): String? = _session.value?.userId
}