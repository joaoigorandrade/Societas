package com.example.Domain.UseCase.Auth

import com.example.Domain.Models.Auth.AuthRequest
import com.example.Domain.Models.Auth.AuthResponse
import com.example.Domain.Repository.Rest.ApiRepositoryInterface
import com.example.Domain.Repository.execute
import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.SocietasRequest

class SignInUseCase(private val repository: ApiRepositoryInterface) {
    suspend fun execute(authRequest: AuthRequest): NetworkResult<AuthResponse> {
        return repository.execute(SocietasRequest.SignIn(authRequest))
    }
}
