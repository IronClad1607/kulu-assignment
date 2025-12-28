package com.ishaan.kuluassignment.networking

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
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

suspend inline fun <reified R> HttpResponse.convertToNetworkResponse(): NetworkResponse<R> {
    if (!this.status.isSuccess()) {
        return NetworkResponse.Error(this.status.description)
    }

    return try {
        NetworkResponse.Success(this.body<R>())
    } catch (e: Exception) {
        NetworkResponse.Error(e.message ?: "Something went wrong!")
    }
}