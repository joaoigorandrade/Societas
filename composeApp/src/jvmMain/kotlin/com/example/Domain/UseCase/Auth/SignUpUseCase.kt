package com.example.Domain.UseCase.Auth

import com.example.Domain.Models.Auth.AuthApiResponse
import com.example.Domain.Models.Auth.AuthRequest
import com.example.Domain.Repository.Rest.ApiRepositoryInterface
import com.example.Domain.Repository.execute
import com.example.Networking.Core.NetworkResult
import com.example.Networking.Interfaces.SocietasRequest

class SignUpUseCase(private val repository: ApiRepositoryInterface) {
    suspend fun execute(authRequest: AuthRequest): NetworkResult<AuthApiResponse> {
        return repository.execute(SocietasRequest.SignUp(authRequest))
    }
}
