package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun textInputColor(isClickable: Boolean, isFocus: Boolean): TextFieldColors {
    val containerColor = MaterialTheme.colorScheme.surfaceContainer
    val placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    val onFieldColor = if (isClickable) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    }
    val borderColor = if (isClickable) {
        Color.Transparent
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    }

    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = Color.Transparent,
        errorBorderColor = if (isFocus) MaterialTheme.colorScheme.error else Color.Transparent,
        focusedContainerColor = containerColor,
        unfocusedContainerColor = containerColor,
        errorContainerColor = containerColor,
        focusedPlaceholderColor = placeholderColor,
        unfocusedPlaceholderColor = placeholderColor,
        disabledContainerColor = if (isClickable) containerColor else Color.Transparent,
        disabledTextColor = onFieldColor,
        disabledBorderColor = borderColor,
        disabledLeadingIconColor = onFieldColor,
        disabledTrailingIconColor = onFieldColor,
        disabledPlaceholderColor = if (isClickable) placeholderColor else MaterialTheme.colorScheme.onSurfaceVariant,
    )
}