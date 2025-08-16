package com.example.Domain.UseCase.Home

import com.example.Domain.Repository.ApiRepositoryInterface
import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.SocietasRequest
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
                val response: NetworkResult<SocietasHomeScreenModel> =
                    withTimeout(timeoutSeconds.seconds) {
                        repository.execute(SocietasRequest.Home)
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
                            delay(1000L * attemptCount)
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