
# Navigation Layer

This document provides a detailed explanation of the design and implementation of the navigation layer, a completely decoupled, standalone module for Jetpack Compose Desktop applications.

## 1. Architecture and Design

The navigation layer is designed to be a completely decoupled, standalone module that can be easily reused in other projects. It follows the SOLID principles and uses a combination of design patterns to achieve this goal.

### 1.1. SOLID Principles

*   **Single Responsibility Principle:** Each class in the navigation layer has a single responsibility. For example, the `NavigationRouter` is responsible for handling navigation requests, the `NavigationStack` is responsible for managing the navigation history, and the `NavigationHost` is responsible for displaying the content of the current route.
*   **Open-Closed Principle:** The navigation layer is open for extension but closed for modification. For example, you can add new routes and navigation guards without modifying the existing code.
*   **Liskov Substitution Principle:** The navigation layer uses interfaces to define the contracts for its components. This allows you to replace the default implementations with your own custom implementations without breaking the application.
*   **Interface Segregation Principle:** The navigation layer provides small, focused interfaces for its components. This makes it easier to use and understand the API.
*   **Dependency Inversion Principle:** The navigation layer uses dependency injection to decouple its components from each other. This makes it easier to test and maintain the code.

### 1.2. Design Patterns

*   **Router Pattern:** The `NavigationRouter` class acts as a central router that maps routes to Composables and handles navigation requests.
*   **Command Pattern:** The navigation actions are encapsulated as commands (e.g., `ForwardCommand`, `BackCommand`, `ReplaceCommand`). This allows for features like undo/redo and logging.
*   **State Machine Pattern:** The navigation state is managed by a `StateMachine` that models the different states of the navigation system (e.g., `Idle`, `Navigating`, `Error`).
*   **MVVM with Navigator:** The navigation layer can be easily integrated with the MVVM pattern. ViewModels can use the `Navigator` interface to request navigation actions.
*   **Dependency Injection:** The navigation layer uses Koin for dependency injection. This makes it easy to provide the navigator and other dependencies to the application.
*   **Observer Pattern:** The navigation layer uses Kotlin Flows to emit events (e.g., route changes). This allows other parts of the application to observe the navigation state and react to changes.

## 2. Project Structure

The navigation layer is organized into the following packages:

*   `api`: Contains the public API of the navigation layer.
*   `core`: Contains the core implementation of the navigation layer.
*   `commands`: Contains the navigation commands.
*   `state`: Contains the navigation state machine.
*   `compose`: Contains the Composable functions and utilities.
*   `guards`: Contains the navigation guards.
*   `di`: Contains the dependency injection module.
*   `utils`: Contains various utility classes.

## 3. Integration

To integrate the navigation layer into your project, you need to follow these steps:

1.  **Add the navigation module as a dependency:** In your `build.gradle.kts` file, add the following dependency:

    ```kotlin
    implementation(project(":navigation-layer"))
    ```

2.  **Initialize the dependency injector:** In your main application class, initialize the Koin dependency injector:

    ```kotlin
    import com.example.Navigation.di.Injector

    fun main() {
        Injector.init()
        // ...
    }
    ```

3.  **Define your routes:** Create a list of `RouteConfig` objects that define the routes in your application:

    ```kotlin
    import com.example.Navigation.api.Route
    import com.example.Navigation.api.RouteConfig

    val routeConfigs = listOf(
        RouteConfig(Route("home")) { HomeScreen() },
        RouteConfig(Route("settings")) { SettingsScreen() }
    )
    ```

4.  **Create a `NavigationHost`:** In your main Composable function, create a `NavigationHost` and build it:

    ```kotlin
    import com.example.Navigation.core.NavigationHost
    import org.koin.java.KoinJavaComponent.getKoin

    @Composable
    fun App() {
        val navigationRouter = getKoin().get<com.example.Navigation.core.NavigationRouter>()
        val navigationHost = NavigationHost(navigationRouter, routeConfigs)
        navigationHost.build()
    }
    ```

## 4. Real-World Use Cases

### 4.1. Authentication Flow

You can use the navigation layer to implement an authentication flow where the user is redirected to a login screen if they are not authenticated.

```kotlin
val routeConfigs = listOf(
    RouteConfig(Route("login")) { LoginScreen() },
    RouteConfig(Route("dashboard")) { DashboardScreen() }
)

val authGuard = object : NavigationGuard {
    override fun canNavigate(route: Route): Boolean {
        val isAuthenticated = // check if the user is authenticated
        return isAuthenticated || route.path == "login"
    }
}

val navigationRouter = NavigationRouter(guards = listOf(authGuard))
```

### 4.2. Multi-Tab Workspace

You can use the navigation layer to implement a multi-tab workspace where each tab has its own navigation stack.

```kotlin
val tab1Navigator = NavigationRouter()
val tab2Navigator = NavigationRouter()

@Composable
fun App() {
    // ...
    TabRow(...) {
        Tab(...) {
            NavigationHost(tab1Navigator, ...)
        }
        Tab(...) {
            NavigationHost(tab2Navigator, ...)
        }
    }
}
```

### 4.4. Deep Linking

You can use the `DeepLinkParser` to handle deep links. The `DeepLinkParser` will parse a URI and return a `Route` object that can be used to navigate to the corresponding screen.

```kotlin
val uri = "app://societas/settings"
val route = DeepLinkParser.parse(uri)
if (route != null) {
    navigator.navigateTo(route)
}
```

### 4.5. Navigation Guards

You can use navigation guards to protect routes from unauthorized access. For example, you can create a `RoleBasedGuard` that checks if a user has a certain role before allowing them to navigate to a route.

```kotlin
val userRoles = listOf("admin")
val requiredRoles = mapOf(Route("admin") to listOf("admin"))
val roleBasedGuard = RoleBasedGuard(userRoles, requiredRoles)

val navigationRouter = NavigationRouter(guards = listOf(roleBasedGuard))

// This will prevent users without the "admin" role from navigating to the "admin" route.
```
