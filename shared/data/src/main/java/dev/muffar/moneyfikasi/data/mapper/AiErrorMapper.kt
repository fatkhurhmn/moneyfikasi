package dev.muffar.moneyfikasi.data.mapper

import dev.muffar.moneyfikasi.domain.model.AiError

fun Throwable.toAiError(): AiError {
    if (this is AiError) return this
    val message = this.message ?: ""
    return when {
        message.contains("404") -> AiError.ModelNotFound
        message.contains("403") -> AiError.InvalidApiKey
        message.contains("429") -> AiError.RateLimited
        message.contains("500") -> AiError.ServerError
        message.contains("Unable to resolve host") || message.contains("No internet") -> AiError.NoInternet
        message.contains("timeout") -> AiError.Timeout
        message.contains("Empty response") -> AiError.EmptyResponse
        else -> AiError.Unknown
    }
}
