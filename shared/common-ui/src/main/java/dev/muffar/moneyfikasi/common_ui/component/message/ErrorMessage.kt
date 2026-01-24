package dev.muffar.moneyfikasi.common_ui.component.message

data class ErrorMessage(
    val message: String? = null,
    val id: Long = System.currentTimeMillis()
) {
    val isNull: Boolean
        get() = message == null
}