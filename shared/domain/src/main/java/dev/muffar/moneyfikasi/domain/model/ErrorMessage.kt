package dev.muffar.moneyfikasi.domain.model

data class ErrorMessage(
    val message: String? = null,
    val resId: Int? = null,
    val id: Long = System.currentTimeMillis()
) {
    val isNull: Boolean
        get() = message == null && resId == null
}
