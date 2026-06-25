package dev.muffar.moneyfikasi.domain.model

sealed class AiError : Exception() {
    data object NoInternet : AiError()
    data object Timeout : AiError()
    data object InvalidApiKey : AiError()
    data object PermissionDenied : AiError()
    data object ModelNotFound : AiError()
    data object RateLimited : AiError()
    data object ServerError : AiError()
    data object EmptyResponse : AiError()
    data object InvalidJson : AiError()
    data object Unknown : AiError()
}
