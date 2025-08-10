package com.example.societas.Controllers

import com.example.societas.Models.Response
import com.example.societas.Services.HealthService

class HealthController(private val healthService: HealthService) {
    fun getHealth(): Response<String> {
        val status = healthService.getHealthStatus()
        return Response(data = status, message = "Server is healthy")
    }
}
