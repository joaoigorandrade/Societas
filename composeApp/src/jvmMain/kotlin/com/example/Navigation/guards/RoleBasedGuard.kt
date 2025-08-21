package com.example.Navigation.guards

import com.example.Navigation.api.Route

class RoleBasedGuard(private val userRoles: List<String>, private val requiredRoles: Map<Route, List<String>>) : NavigationGuard {
    override fun canNavigate(route: Route): Boolean {
        val required = requiredRoles[route] ?: return true
        return userRoles.any { required.contains(it) }
    }
}