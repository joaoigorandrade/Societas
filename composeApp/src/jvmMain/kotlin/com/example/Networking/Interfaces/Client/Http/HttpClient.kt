package com.example.Networking.Interfaces.Client.Http

import com.example.Networking.Core.NetworkResult
import kotlinx.coroutines.flow.Flow

interface HttpClient {
    suspend fun <T> get(
        endpoint: String,
        headers: Map<String, String> = emptyMap(),
        queryParams: Map<String, String> = emptyMap()
    ): NetworkResult<T>
    
    suspend fun <T, R> post(
        endpoint: String,
        body: T,
        headers: Map<String, String> = emptyMap()
    ): NetworkResult<R>
    
    suspend fun <T, R> put(
        endpoint: String,
        body: T,
        headers: Map<String, String> = emptyMap()
    ): NetworkResult<R>
    
    suspend fun <T, R> patch(
        endpoint: String,
        body: T,
        headers: Map<String, String> = emptyMap()
    ): NetworkResult<R>
    
    suspend fun delete(
        endpoint: String,
        headers: Map<String, String> = emptyMap()
    ): NetworkResult<Unit>
    
    suspend fun <T> head(
        endpoint: String,
        headers: Map<String, String> = emptyMap()
    ): NetworkResult<Map<String, List<String>>>
    
    suspend fun <T> options(
        endpoint: String,
        headers: Map<String, String> = emptyMap()
    ): NetworkResult<Map<String, List<String>>>
}