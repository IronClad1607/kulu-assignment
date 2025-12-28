package com.ishaan.kuluassignment.networking

import com.ishaan.kuluassignment.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// Http Client Provider for API Call using Ktor
class ClientProvider {
    val client = HttpClient {
        // Configure JSON serialization
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
                explicitNulls = false
                encodeDefaults = true
            })
        }

        // Log Request and Response
        install(LoggingPlugin)

        // Base URL Configuration
        defaultRequest {
            url {
                takeFrom(BaseURL.API_BASE_URL)
                parameters.append("api_key", BuildConfig.TMDB_API_KEY)
                parameters.append("language", "en-US")
            }
        }
    }
}