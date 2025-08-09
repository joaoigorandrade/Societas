package com.example.Networking.DependencyInjection

import com.example.Networking.Core.Configurations.NetworkConfiguration
import com.example.Networking.Core.Configurations.WebSocketConfiguration
import com.example.Networking.Implementation.KtorHttpClient
import com.example.Networking.Implementation.KtorWebSocketClient
import com.example.Networking.Interceptors.*
import com.example.Networking.Interfaces.*
import com.example.Networking.Interfaces.Interceptors.*
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
        AuthenticationInterceptorImpl(get())
    }
    
    single<LoggingInterceptor> {
        LoggingInterceptorImpl(
            logger = ConsoleNetworkLogger(),
            logLevel = LogLevel.INFO
        )
    }
    
    single<RetryInterceptor> {
        RetryInterceptorImpl(
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
    
    single<StreamingHttpClient> {
        get<KtorHttpClient>()
    }
    
    single<WebSocketClient> {
        KtorWebSocketClient(
            config = get<WebSocketConfiguration>()
        )
    }
    
    single<NetworkManager> {
        NetworkManagerImpl(
            httpClient = get(),
            streamingClient = get(),
            webSocketClient = get()
        )
    }
}
class InMemoryTokenProvider : TokenProvider {
    private var accessToken: String? = null
    private var refreshToken: String? = null
    
    override suspend fun getAccessToken(): String? = accessToken
    
    override suspend fun getRefreshToken(): String? = refreshToken
    
    override suspend fun saveTokens(accessToken: String, refreshToken: String?) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }
    
    override suspend fun refreshTokens(refreshToken: String): TokenPair {
        return TokenPair(
            accessToken = "new_access_token",
            refreshToken = "new_refresh_token"
        )
    }
    
    override suspend fun clearTokens() {
        accessToken = null
        refreshToken = null
    }
}

interface NetworkManager {
    val httpClient: HttpClient
    val streamingClient: StreamingHttpClient
    val webSocketClient: WebSocketClient
    
    suspend fun initialize()
    suspend fun cleanup()
}

class NetworkManagerImpl(
    override val httpClient: HttpClient,
    override val streamingClient: StreamingHttpClient,
    override val webSocketClient: WebSocketClient
) : NetworkManager {
    
    override suspend fun initialize() {
    }
    
    override suspend fun cleanup() {
        if (httpClient is KtorHttpClient) {
            httpClient.close()
        }
        if (webSocketClient is KtorWebSocketClient) {
            webSocketClient.close()
        }
    }
}
