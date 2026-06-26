package dev.muffar.moneyfikasi.data.mapper

import dev.muffar.moneyfikasi.data.remote.groq.GroqErrorResponse
import dev.muffar.moneyfikasi.domain.model.AiError
import dev.muffar.moneyfikasi.domain.model.AiException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toAiError(): AiError {
    if (this is AiException) return this.error

    return when (this) {
        is HttpException -> {
            val errorResponse = response()?.errorBody()?.string()?.let {
                try {
                    Json { ignoreUnknownKeys = true }.decodeFromString<GroqErrorResponse>(it)
                } catch (e: Exception) {
                    null
                }
            }

            if (errorResponse != null) {
                val error = when (errorResponse.error.type) {
                    "authentication_error" -> AiError.InvalidApiKey
                    "permission_error" -> AiError.PermissionDenied
                    "rate_limit_error" -> AiError.RateLimited
                    "invalid_request_error" -> AiError.InvalidRequest
                    else -> null
                }
                if (error != null) return error
            }

            when (code()) {
                400 -> AiError.InvalidRequest
                401 -> AiError.InvalidApiKey
                403 -> AiError.PermissionDenied
                404 -> AiError.ModelNotFound
                408 -> AiError.Timeout
                413 -> AiError.InvalidRequest
                422 -> AiError.InvalidRequest
                429 -> AiError.RateLimited
                500, 502, 503 -> AiError.ServerError
                504 -> AiError.Timeout
                else -> AiError.Unknown
            }
        }

        is UnknownHostException, is ConnectException -> AiError.NoInternet
        is SocketTimeoutException -> AiError.Timeout
        is IOException -> AiError.NoInternet
        else -> AiError.Unknown
    }
}
