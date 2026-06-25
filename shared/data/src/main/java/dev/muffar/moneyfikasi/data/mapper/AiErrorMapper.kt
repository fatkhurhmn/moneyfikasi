package dev.muffar.moneyfikasi.data.mapper

import android.util.Log
import dev.muffar.moneyfikasi.domain.model.AiError
import dev.muffar.moneyfikasi.domain.model.AiException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toAiError(): AiError {
    if (this is AiException) return this.error

    Log.d("AiErrorMapper", "toAiError: ${this.message}")
    val message = this.message ?: ""
    return when {
        this is UnknownHostException -> AiError.NoInternet
        this is SocketTimeoutException -> AiError.Timeout
        this is IOException -> AiError.NoInternet
        message.contains("404") -> AiError.ModelNotFound
        message.contains("403") -> AiError.PermissionDenied
        message.contains("401") || message.contains("API key") -> AiError.InvalidApiKey
        message.contains("429") -> AiError.RateLimited
        message.contains("500") || message.contains("503") -> AiError.ServerError
        message.contains("Empty response") -> AiError.EmptyResponse
        else -> AiError.Unknown
    }
}
