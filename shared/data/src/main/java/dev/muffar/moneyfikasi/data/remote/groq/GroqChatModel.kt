package dev.muffar.moneyfikasi.data.remote.groq

import kotlinx.serialization.Serializable

@Serializable
data class GroqChatRequest(
    val model: String,
    val messages: List<GroqMessage>
)

@Serializable
data class GroqMessage(
    val role: String,
    val content: String
)

@Serializable
data class GroqChatResponse(
    val choices: List<GroqChoice>
)

@Serializable
data class GroqChoice(
    val message: GroqMessage
)

@Serializable
data class GroqErrorResponse(
    val error: GroqError
)

@Serializable
data class GroqError(
    val message: String,
    val type: String,
    val code: String? = null
)
