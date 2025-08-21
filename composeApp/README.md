# Societas - Compose for Desktop Application

## 1. Project Overview

Societas is a desktop application built with **Compose for Desktop** that demonstrates a modern, clean, and scalable architecture. It provides a chat-style interface where a user can interact with virtual C-suite agents (like a CFO) to query enterprise data. The project serves as a robust template for building production-quality desktop applications.

## 2. Architecture

This application is built using the **MVVM (Model-View-ViewModel)** pattern, heavily influenced by **Clean Architecture** principles. This design enforces a strict separation of concerns, ensuring the codebase is modular, testable, and easy to maintain.

The data flows in a unidirectional stream:

**UI (View) → ViewModel → UseCase → Repository → Data Source (Network)**

-   **View (Compose UI)**: Renders the state and sends user events to the ViewModel.
-   **ViewModel**: Manages and exposes UI state as a `StateFlow`. It calls UseCases in response to UI events.
-   **UseCase (Domain)**: Contains specific business logic. It orchestrates data from one or more repositories.
-   **Repository (Domain/Data)**: Abstract interface for a data source. Its implementation handles the actual data operations.
-   **Data Source (Networking)**: The concrete implementation, in this case, a Ktor-based HTTP client that communicates with a remote server.

### Core Technologies

-   **UI Framework**: Jetpack Compose for Desktop
-   **Dependency Injection**: Koin
-   **Networking**: Ktor
-   **Asynchronous Operations**: Kotlin Coroutines & `StateFlow`
-   **Serialization**: `kotlinx.serialization`

## 3. Project Structure

The `composeApp` module is organized into packages that reflect its clean architecture:

-   `Core/`: The application's entry point (`main.kt`) and the root Koin DI module (`KoinModule.kt`).
-   `UI/`: All UI-related code.
    -   `Screens/`: Each screen has its own package containing:
        -   `SocietasHomeScreen.kt`: The main Composable function for the screen.
        -   `SocietasHomeScreenViewModel.kt`: The ViewModel managing the screen's state.
        -   `SocietasViewState.kt`: A sealed interface defining all possible states for the screen (e.g., `Loading`, `Success`, `Error`).
        -   `Components/`: Screen-specific, reusable Composables.
    -   `Components/`: Global, reusable UI components (e.g., `SocietasChatMessage`, `SocietasMessageInput`).
-   `Domain/`: The core business logic, completely independent of UI and data frameworks.
    -   `UseCase/`: Contains UseCase classes that encapsulate specific business rules (e.g., `SocietasHomeUseCase`).
    -   `Repository/`: Defines repository interfaces (`ApiRepositoryInterface`) and includes the concrete implementation (`ApiRepository`).
-   `Networking/`: A self-contained, reusable networking module.
    -   `Core/`: Defines foundational types like `NetworkResult`, `NetworkException`, and `NetworkConfiguration`.
    -   `Interfaces/`: Defines contracts for requests (`RequestInterface`) and the networking client (`NetworkingOperationInterface`).
    -   `Implementation/`: The Ktor-based implementation of the networking client.
-   `Navigation/`: A custom-built, decoupled navigation framework.

## 4. Deep Dive into Core Modules

### Networking Layer

The networking layer is designed to be robust and reusable. It is built on Ktor.

-   **Type-Safe Requests**: API endpoints are defined as objects or enums implementing the `RequestInterface`. This provides compile-time safety and a clear definition of each endpoint's properties (path, method, parameters).

    ```kotlin
    // From: Domain/SocietasRequest.kt
    enum class SocietasRequest : RequestInterface {
        Home {
            override val method: HttpMethod get() = HttpMethod.Get
            override val path: String get() = "/home"
            // ...
        }
    }
    ```

-   **Robust Result Handling**: All network calls return a `NetworkResult<T>` sealed class. This forces the caller (typically a repository or use case) to handle all possible outcomes explicitly: `Success`, `Error`, and `Loading`.

    ```kotlin
    // From: Networking/Core/NetworkResult.kt
    sealed class NetworkResult<out T> {
        data class Success<T>(val data: T) : NetworkResult<T>()
        data class Error(val exception: NetworkException, ...) : NetworkResult<Nothing>()
        data class Loading(...) : NetworkResult<Nothing>()
    }
    ```

-   **Centralized Ktor Client**: The `NetworkingOperation` class configures and encapsulates the Ktor `HttpClient`. It uses Ktor plugins for:
    -   `ContentNegotiation`: Automatically serializes/deserializes data using `kotlinx.serialization.json`.
    -   `Logging`: Logs network requests and responses for debugging.
    -   `HttpTimeout`: Configures global request, connection, and socket timeouts.

### Domain Layer

This layer orchestrates the flow of data and applies business rules.

-   **UseCases**: The `SocietasHomeUseCase` is a prime example. It contains business logic that is more complex than a simple data fetch. It implements its own **retry and timeout mechanism** that wraps the repository call, ensuring resilience.

    ```kotlin
    // From: Domain/UseCase/Home/SocietasHomeUseCase.kt
    class SocietasHomeUseCase(private val repository: ApiRepositoryInterface) {
        suspend fun execute(): SocietasViewState {
            // Implements a while loop for retries and withTimeout for deadlines
        }
    }
    ```

-   **Repositories**: The `ApiRepository` implements the `ApiRepositoryInterface` and acts as a facade, delegating its calls to the `NetworkingOperationInterface`. This decouples the domain layer from the specific Ktor implementation.

-   **Typed Execution**: The `execute<T>` extension function on `ApiRepositoryInterface` provides a convenient, type-safe way to perform API calls and have the JSON response automatically deserialized into a Kotlin data class. It also gracefully catches and maps different exception types (`SerializationException`, `ClientRequestException`, etc.) to a `NetworkResult.Error`.

### UI and State Management

The UI is purely a function of the state, following a declarative pattern.

-   **State as a Sealed Interface**: `SocietasViewState` defines the contract for the UI. The screen can only be in one of these states at any given time, which eliminates invalid state combinations.

-   **ViewModel and StateFlow**: `SocietasHomeScreenViewModel` holds a `MutableStateFlow<SocietasViewState>`. When fetching data, it first emits `Loading`, and then, upon completion, updates the flow with either `Success(data)` or `Error(message)`.

-   **Declarative UI**: The `SocietasHomeScreen` Composable subscribes to the ViewModel's `StateFlow` using `collectAsState()`. A `when` block handles which Composable to display for the current state, ensuring the UI always reflects the application's state.

    ```kotlin
    // From: UI/Screens/Home/SocietasHomeScreen.kt
    val uiState = viewModel.uiState.collectAsState()

    when(val state = uiState.value) {
        is SocietasViewState.Success -> { ... }
        is SocietasViewState.Loading -> { CircularProgressIndicator() }
        is SocietasViewState.Error -> { Text(text = state.message) }
    }
    ```

### Dependency Injection with Koin

Koin is used to wire all the components together.

-   **Modular Configuration**: Each architectural layer has its own Koin module (`networkModule`, `domainModule`, `uiModule`).
-   **Providing Dependencies**: Dependencies are declared as `single` (singleton) or `factory` (new instance every time).
-   **Named Injections**: The `networkModule` provides different `NetworkConfiguration` instances using named qualifiers (`development`, `production`), allowing the application to be configured for different environments easily.
-   **Injection**: Dependencies are injected into classes via their constructors. In Composable functions, the `koinInject()` delegate is used to retrieve instances like ViewModels.

## 5. Setup and Run

To build and run the application, execute the following Gradle command from the project's root directory:

```bash
./gradlew :composeApp:run
```
