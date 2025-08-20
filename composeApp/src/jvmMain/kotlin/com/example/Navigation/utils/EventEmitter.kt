package com.example.Navigation.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EventEmitter<T> {
    private val _events = MutableSharedFlow<T>()
    val events = _events.asSharedFlow()

    suspend fun emit(event: T) {
        _events.emit(event)
    }
}