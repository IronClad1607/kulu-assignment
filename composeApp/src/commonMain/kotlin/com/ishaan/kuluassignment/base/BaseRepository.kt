package com.ishaan.kuluassignment.base

import com.ishaan.kuluassignment.networking.ClientProvider
import com.ishaan.kuluassignment.networking.NetworkResponse
import com.ishaan.kuluassignment.networking.convertToNetworkResponse
import com.ishaan.kuluassignment.utils.Logger
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

// Base repository class for making network calls
abstract class BaseRepository(
    val clientProvider: ClientProvider
) {
    /**
     * Makes a network call with the specified method, endpoint, and query parameters.
     * @param method The HTTP method for the network call.
     * @param endpoint The endpoint for the network call.
     * @param queryParams The query parameters for the network call.
     * @return A [NetworkResponse] representing the result of the network call.
     */
    protected suspend inline fun <reified R : Any> makeNetworkCall(
        method: HttpMethod,
        endpoint: String,
        queryParams: Map<String, Any>? = null,
    ): NetworkResponse<R> {
        return withContext(Dispatchers.IO) {
            try {
                val response = clientProvider.client.request {
                    this.method = method
                    url {
                        appendPathSegments(endpoint)

                        queryParams?.forEach { (key, value) ->
                            parameters.append(key, value.toString())
                        }
                    }
                }
                return@withContext response.convertToNetworkResponse<R>()
            } catch (e: Exception) {
                Logger.errorLog.e {
                    "Error making network call: $e"
                }
                NetworkResponse.Error(e.message ?: "Something went wrong!")
            }
        }
    }
}