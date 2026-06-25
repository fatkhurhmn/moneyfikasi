package dev.muffar.moneyfikasi.common_ui.utils

import dev.muffar.moneyfikasi.domain.model.AiError
import dev.muffar.moneyfikasi.resource.R

fun AiError.toMessageRes(): Int {
    return when (this) {
        is AiError.NoInternet -> R.string.error_ai_no_internet
        is AiError.Timeout -> R.string.error_ai_timeout
        is AiError.InvalidApiKey -> R.string.error_ai_invalid_api_key
        is AiError.PermissionDenied -> R.string.error_ai_permission_denied
        is AiError.ModelNotFound -> R.string.error_ai_model_not_found
        is AiError.RateLimited -> R.string.error_ai_rate_limited
        is AiError.ServerError -> R.string.error_ai_server_error
        is AiError.EmptyResponse -> R.string.error_ai_empty_response
        is AiError.InvalidJson -> R.string.error_ai_invalid_json
        is AiError.Unknown -> R.string.error_ai_unknown
    }
}
