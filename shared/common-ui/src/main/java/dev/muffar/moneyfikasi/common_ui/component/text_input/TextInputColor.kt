package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun textInputColor(isClickable: Boolean, isFocus: Boolean) =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = Color.Transparent,
        errorBorderColor = if (isFocus) MaterialTheme.colorScheme.error else Color.Transparent,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.8f),
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.8f),
        errorContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.8f),
        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledContainerColor = if (isClickable) {
            MaterialTheme.colorScheme.surfaceVariant.copy(0.8f)
        } else {
            Color.Transparent
        },
        disabledTextColor = if (isClickable) {
            MaterialTheme.colorScheme.onBackground
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        disabledBorderColor = if (isClickable) {
            MaterialTheme.colorScheme.surfaceVariant.copy(0.8f)
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        disabledLeadingIconColor = if (isClickable) {
            MaterialTheme.colorScheme.onBackground
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        disabledTrailingIconColor = if (isClickable) {
            MaterialTheme.colorScheme.onBackground
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )