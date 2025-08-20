package com.example.Navigation.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StateFlowManager(
    initialState: NavigationState
) : StateMachine {
    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<NavigationState> = _state

    override fun transition(newState: NavigationState) {
        _state.value = newState
    }
}