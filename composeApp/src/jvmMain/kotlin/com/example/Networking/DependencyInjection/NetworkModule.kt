package com.example.Networking.DependencyInjection

import com.example.Networking.Core.Configurations.NetworkConfiguration
import com.example.Networking.Core.Configurations.WebSocketConfiguration
import com.example.Networking.Implementation.KtorHttpClient
import com.example.Networking.Implementation.KtorWebSocketClient
import com.example.Networking.Implementation.KtorStreamingHttpClient
import com.example.Networking.Interfaces.Interceptors.*
import com.example.Networking.Interfaces.Client.Http.HttpClient
import com.example.Networking.Interfaces.Client.WebSocket.WebSocketClient
import com.example.Networking.Interfaces.Client.StreamingHttpClient
import com.example.Networking.Token.InMemoryTokenProvider
import com.example.Networking.Interceptors.Authentication.TokenProvider
import com.example.Networking.Interceptors.Retry.RetryInterceptorImplementation
import com.example.Networking.Interceptors.Logging.LoggingInterceptorImplementation
import com.example.Networking.Interceptors.Authentication.AuthenticationInterceptorImplementation
import com.example.Networking.Interceptors.Logging.LogLevel
import com.example.Networking.Interceptors.Logging.ConsoleNetworkLogger
import com.example.Networking.Manager.NetworkManager
import com.example.Networking.Manager.NetworkManagerImplementation
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    
    single<NetworkConfiguration>(named("development")) {
        NetworkConfiguration.development("http://localhost:8080")
    }
    
    single<NetworkConfiguration>(named("production")) {
        NetworkConfiguration.production("https://api.societas.com")
    }
    
    single<WebSocketConfiguration> {
        WebSocketConfiguration(
            url = "ws://localhost:8080/websocket",
            pingInterval = 20_000L,
            reconnectAttempts = 5
        )
    }
    
    single<TokenProvider> {
        InMemoryTokenProvider()
    }
    
    single<AuthenticationInterceptor> {
        AuthenticationInterceptorImplementation(get())
    }
    
    single<LoggingInterceptor> {
        LoggingInterceptorImplementation(
            logger = ConsoleNetworkLogger(),
            logLevel = LogLevel.INFO
        )
    }
    
    single<RetryInterceptor> {
        RetryInterceptorImplementation(
            maxRetries = 3,
            baseDelay = 1000L
        )
    }
    
    single<List<NetworkInterceptor>> {
        listOf(
            get<AuthenticationInterceptor>(),
            get<LoggingInterceptor>(),
            get<RetryInterceptor>()
        )
    }
    
    single<HttpClient> {
        KtorHttpClient(
            config = get<NetworkConfiguration>(named("development")),
            interceptors = get()
        )
    }
    
    single<WebSocketClient> {
        KtorWebSocketClient(
            config = get<WebSocketConfiguration>()
        )
    }
    
    single<StreamingHttpClient> {
        KtorStreamingHttpClient(
                    client = (get<HttpClient>() as KtorHttpClient).client
        )
    }
    
    single<NetworkManager> {
        NetworkManagerImplementation(
            httpClient = get(),
            streamingClient = get(),
            webSocketClient = get()
        )
    }
}