package dev.muffar.moneyfikasi.domain.model

import dev.muffar.moneyfikasi.resource.R

sealed interface AiError {
    data object NoInternet : AiError
    data object Timeout : AiError
    data object InvalidApiKey : AiError
    data object PermissionDenied : AiError
    data object ModelNotFound : AiError
    data object RateLimited : AiError
    data object ServerError : AiError
    data object EmptyResponse : AiError
    data object InvalidJson : AiError
    data object Unknown : AiError

    fun toMessageRes(): Int = when (this) {
        NoInternet -> R.string.ai_error_no_internet
        Timeout -> R.string.ai_error_timeout
        InvalidApiKey -> R.string.ai_error_invalid_api_key
        PermissionDenied -> R.string.ai_error_permission_denied
        ModelNotFound -> R.string.ai_error_model_not_found
        RateLimited -> R.string.ai_error_rate_limited
        ServerError -> R.string.ai_error_server_error
        EmptyResponse -> R.string.ai_error_empty_response
        InvalidJson -> R.string.ai_error_invalid_json
        Unknown -> R.string.ai_error_unknown
    }
}

class AiException(
    val error: AiError,
    override val cause: Throwable? = null,
) : Exception(cause)
