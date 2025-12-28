package com.ishaan.kuluassignment.base

import com.ishaan.kuluassignment.networking.ClientProvider
import com.ishaan.kuluassignment.networking.NetworkResponse
import com.ishaan.kuluassignment.networking.convertToNetworkResponse
import com.ishaan.kuluassignment.utils.Logger
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

abstract class BaseRepository(
    val clientProvider: ClientProvider
) {
    protected suspend inline fun <reified R : Any> makeNetworkCall(
        method: HttpMethod,
        endpoint: String,
        queryParams: Map<String, Any>? = null,
    ): NetworkResponse<R> {
        return withContext(Dispatchers.IO) {
            try {
                val response = clientProvider.client.request(endpoint) {
                    this.method = method
                    queryParams?.forEach { (key, value) ->
                        url.parameters.append(key, value.toString())
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