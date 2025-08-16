package com.example.Domain.UseCase.Home

import com.example.Domain.Repository.ApiRepositoryInterface
import com.example.Networking.Core.NetworkResult
import com.example.UI.Screens.Home.SocietasHomeScreenModel
import com.example.UI.Screens.SocietasViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.seconds

class SocietasHomeUseCase(
    private val repository: ApiRepositoryInterface,
    private val maxRetries: Int = 3,
    private val timeoutSeconds: Long = 30
) {
    suspend fun execute(): SocietasViewState {
        var attemptCount = 0

        while (attemptCount < maxRetries) {
            try {
                val response = withTimeout(timeoutSeconds.seconds) {
                    repository.post<Map<String, String>, SocietasHomeScreenModel>(
                        "http://localhost:3000/home",
                        mapOf("userId" to "user1")
                    )
                }

                return when (response) {
                    is NetworkResult.Success -> {
                        if (isValidResponse(response.data)) {
                            SocietasViewState.Success(response.data)
                        } else {
                            SocietasViewState.Error("Invalid response data received")
                        }
                    }

                    is NetworkResult.Error -> {
                        attemptCount++
                        if (attemptCount >= maxRetries) {
                            SocietasViewState.Error("API call failed after $maxRetries attempts: ${response.message}")
                        } else {
                            delay(1000L * attemptCount) // Exponential backoff
                            continue
                        }
                    }

                    is NetworkResult.Loading -> {
                        SocietasViewState.Loading
                    }
                }
            } catch (e: Exception) {
                attemptCount++
                if (attemptCount >= maxRetries) {
                    return SocietasViewState.Error("Exception occurred: ${e.message}")
                } else {
                    delay(1000L * attemptCount)
                    continue
                }
            }
        }

        return SocietasViewState.Error("Failed to execute API call after $maxRetries attempts")
    }

    private fun isValidResponse(data: SocietasHomeScreenModel): Boolean {
        return data.userName.isNotBlank() &&
                data.enterprise.isNotBlank() &&
                data.cBoard.isNotEmpty()
    }
}