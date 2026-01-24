package dev.muffar.moneyfikasi.domain.model

data class ErrorMessage(
    val message: String? = null,
    val id: Long = System.currentTimeMillis()
) {
    val isNull: Boolean
        get() = message == null
}