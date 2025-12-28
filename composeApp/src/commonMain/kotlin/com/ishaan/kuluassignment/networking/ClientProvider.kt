package com.ishaan.kuluassignment.networking

import com.ishaan.kuluassignment.BuildConfig
import com.ishaan.kuluassignment.utils.platform.PlatformSettings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ClientProvider(
    platformSettings: PlatformSettings
) {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
                explicitNulls = false
                encodeDefaults = true
            })
        }

        install(LoggingPlugin)

        defaultRequest {
            url(BaseURL.API_BASE_URL) {
                parameters.append("api_key", BuildConfig.TMDB_API_KEY)
                parameters.append("language", "en-US")
            }
        }
    }
}