package com.ishaan.kuluassignment.networking

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

sealed class NetworkResponse<out T> {
    /**
     * Represents a successful response with parsed data.
     */
    data class Success<T>(val data: T) : NetworkResponse<T>()

    /**
     * Represents an error response with HTTP status code and message.
     */
    data class Error(val message: String) : NetworkResponse<Nothing>()
}

suspend inline fun <reified T> HttpResponse.convertToNetworkResponse(): NetworkResponse<T> {
    return try {
        if (status.isSuccess()) {
            val data = body<T>()
            NetworkResponse.Success(data)
        } else {
            NetworkResponse.Error("Network error: ${status.value}")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        NetworkResponse.Error("Parsing error: ${e.message}")
    }
}