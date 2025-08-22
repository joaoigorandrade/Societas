package com.example.Navigation.core

import androidx.compose.runtime.mutableStateListOf
import com.example.Navigation.api.Route

class NavigationStack {
    private val stack = mutableStateListOf<Route>()

    fun push(route: Route) {
        stack.add(route)
    }

    fun pop() {
        if (stack.isNotEmpty()) {
            stack.removeAt(stack.lastIndex)
        }
    }

    fun replace(route: Route) {
        if (stack.isNotEmpty()) {
            stack[stack.lastIndex] = route
        } else {
            push(route)
        }
    }

    fun current(): Route? {
        return stack.lastOrNull()
    }
    
    fun getStack(): List<Route> {
        return stack.toList()
    }
}