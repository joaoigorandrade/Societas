package com.example.Navigation.compose

import androidx.compose.runtime.Composable

@Composable
fun TransitionHandler(content: @Composable () -> Unit) {
    content()
}