package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import dev.muffar.moneyfikasi.domain.model.ErrorMessage

@Composable
fun CommonTextInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String,
    error: ErrorMessage = ErrorMessage(),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isClickable: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onClick: () -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
    ) {
        TextInputLabel(label)
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() },
            value = value,
            onValueChange = { onValueChange(it.trimStart()) },
            isError = error.message != null,
            shape = MaterialTheme.shapes.medium,
            colors = textInputColor(isClickable),
            placeholder = { Text(text = placeholder) },
            enabled = if (isClickable) false else enabled,
            readOnly = if (isClickable) true else readOnly,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            leadingIcon = leadingIcon
        )
        TextInputError(error)
    }
}

@Composable
fun CommonTextInput(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String? = null,
    placeholder: String,
    error: ErrorMessage = ErrorMessage(),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    Column(
        modifier = modifier
    ) {
        TextInputLabel(label)
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            shape = MaterialTheme.shapes.medium,
            isError = error.message != null,
            colors = textInputColor(false),
            placeholder = { Text(text = placeholder) },
            enabled = enabled,
            readOnly = readOnly,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
        )
        TextInputError(error)
    }
}

@Composable
private fun textInputColor(isClickable: Boolean) =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        errorBorderColor = Color.Transparent,
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
