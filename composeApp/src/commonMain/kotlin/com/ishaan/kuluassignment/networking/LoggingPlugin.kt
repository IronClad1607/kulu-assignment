package com.ishaan.kuluassignment.networking

import com.ishaan.kuluassignment.utils.Logger
import io.ktor.client.plugins.api.SendingRequest
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.content.OutgoingContent
import io.ktor.http.content.TextContent
import io.ktor.util.AttributeKey
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.readRemaining
import io.ktor.utils.io.readText
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * A Ktor HTTP client plugin for logging outgoing requests and incoming responses.
 * Logs include URL, method, headers, body content, and response time.
 */
@OptIn(ExperimentalTime::class)
val LoggingPlugin = createClientPlugin("LoggingPlugin") {
    val onCallTimeKey = AttributeKey<Long>("onCallTimeKey")

    // Logs outgoing HTTP request details including URL, method, headers, body, and timestamp.
    on(SendingRequest) { request, content ->
        // Capture the current time to calculate response duration later.
        val onCallTime = Clock.System.now().toEpochMilliseconds()
        request.attributes.put(onCallTimeKey, onCallTime)

        val logBuilder = StringBuilder()

        logBuilder.appendLine("🔍 [HTTP REQUEST START]")
        logBuilder.appendLine("📤 URL: ${request.url}")
        logBuilder.appendLine("📬 METHOD: ${request.method.value}")
        logBuilder.appendLine("🧾 HEADERS:")
        request.headers.entries().forEach { entry ->
            logBuilder.appendLine("   ${entry.key}: ${entry.value.joinToString("; ")}")
        }
        logBuilder.appendLine("📦 BODY LENGTH: ${content.contentLength}")
        logBuilder.appendLine("📝 BODY:")
        // Extract request body as a string, handling various content types.
        logBuilder.appendLine(getRequestString(content))
        logBuilder.appendLine("🏁 [HTTP REQUEST END]")

        Logger.networkLog.d { logBuilder.toString() }
    }

    // Logs incoming HTTP response details including URL, method, status, headers, body, and response time.
    onResponse { response ->
        val onCallTime = response.call.attributes[onCallTimeKey]
        // Capture the response received time and calculate the duration since request was sent.
        val onCallReceiveTime = Clock.System.now().toEpochMilliseconds()
        val timeTaken = onCallReceiveTime - onCallTime

        val logBuilder = StringBuilder()

        logBuilder.appendLine("🛰️ [HTTP RESPONSE START]")
        logBuilder.appendLine("📥 URL: ${response.request.url}")
        logBuilder.appendLine("📬 METHOD: ${response.request.method.value}")
        logBuilder.appendLine("🎯 STATUS: ${response.status}")
        logBuilder.appendLine("⏱️ RESPONSE TIME: ${timeTaken}ms")
        logBuilder.appendLine("🧾 HEADERS:")
        response.headers.entries().forEach { entry ->
            logBuilder.appendLine("   ${entry.key}: ${entry.value.joinToString("; ")}")
        }
        logBuilder.appendLine("📝 BODY:")
        logBuilder.appendLine(response.bodyAsText())
        logBuilder.appendLine("🏁 [HTTP RESPONSE END]")

        Logger.networkLog.d { logBuilder.toString() }
    }
}


/**
 * Converts the outgoing HTTP content into a readable string for logging.
 *
 * @param content The HTTP request content.
 * @return The string representation of the request body.
 */
private suspend fun getRequestString(content: OutgoingContent): String {
    return when (content) {
        // Handle ByteArray content type
        is OutgoingContent.ByteArrayContent -> content.bytes().decodeToString()
        // Handle ReadChannel content type
        is OutgoingContent.ReadChannelContent -> content.readFrom().readRemaining().readText()
        // Handle WriteChannel content type
        is OutgoingContent.WriteChannelContent -> {
            val channel = ByteChannel(true)
            content.writeTo(channel)
            channel.readRemaining().readText()
        }
        // Handle simple text content
        is TextContent -> content.text
        // Fallback for unhandled content types
        else -> "[⚠️ Unlogged content type: ${content::class.simpleName}]"
    }
}