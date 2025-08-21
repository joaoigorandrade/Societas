package com.example.Navigation.state

import kotlinx.coroutines.flow.StateFlow

interface StateMachine {
    val state: StateFlow<NavigationState>
    fun transition(newState: NavigationState)
}