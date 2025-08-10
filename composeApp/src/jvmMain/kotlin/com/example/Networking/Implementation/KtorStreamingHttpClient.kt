package com.example.Networking.Implementation

import com.example.Networking.Core.NetworkResult
import com.example.Networking.Core.NetworkException
import com.example.Networking.Interfaces.Client.StreamingHttpClient
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
class KtorStreamingHttpClient(
    private val client: HttpClient
) : StreamingHttpClient {
    
    override fun <T> getStream(
        endpoint: String,
        headers: Map<String, String>
    ): Flow<NetworkResult<T>> = flow {
        try {
            val response = client.get(endpoint) {
                headers.forEach { (key, value) ->
                    header(key, value)
                }
            }
            
            if (response.status.isSuccess()) {
                val content = response.bodyAsText()
                // This is a simplified implementation - in reality you'd need proper type handling
                @Suppress("UNCHECKED_CAST")
                emit(NetworkResult.Success(content as T))
            } else {
                                emit(NetworkResult.Error(NetworkException.HttpError(response.status.value, response.status.description)))
            }
        } catch (e: Exception) {
                        emit(NetworkResult.Error(NetworkException.UnknownError(e.message ?: "Unknown error")))
        }
    }
    
    override suspend fun <T> uploadFile(
        endpoint: String,
        fileData: ByteArray,
        fileName: String,
        mimeType: String,
        headers: Map<String, String>,
        onProgress: (Float) -> Unit
    ): NetworkResult<T> {
        return try {
            val response = client.post(endpoint) {
                headers.forEach { (key, value) ->
                    header(key, value)
                }
                setBody(fileData)
                contentType(ContentType.parse(mimeType))
            }
            
            if (response.status.isSuccess()) {
                val content = response.bodyAsText()
                @Suppress("UNCHECKED_CAST")
                NetworkResult.Success(content as T)
            } else {
                                NetworkResult.Error(NetworkException.HttpError(response.status.value, response.status.description))
            }
        } catch (e: Exception) {
                        NetworkResult.Error(NetworkException.UnknownError(e.message ?: "Upload failed"))
        }
    }
    
    override suspend fun downloadFile(
        endpoint: String,
        headers: Map<String, String>,
        onProgress: (Float) -> Unit
    ): NetworkResult<ByteArray> {
        return try {
            val response = client.get(endpoint) {
                headers.forEach { (key, value) ->
                    header(key, value)
                }
            }
            
            if (response.status.isSuccess()) {
                val bytes = response.readRawBytes()
                NetworkResult.Success(bytes)
            } else {
                                NetworkResult.Error(NetworkException.HttpError(response.status.value, response.status.description))
            }
        } catch (e: Exception) {
                        NetworkResult.Error(NetworkException.UnknownError(e.message ?: "Download failed"))
        }
    }
}
