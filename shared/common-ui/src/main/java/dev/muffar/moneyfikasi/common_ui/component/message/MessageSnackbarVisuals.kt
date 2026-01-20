package dev.muffar.moneyfikasi.common_ui.component.message

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

data class MessageSnackbarVisuals(
    override val message: String,
    val type: SnackbarType,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short
) : SnackbarVisuals