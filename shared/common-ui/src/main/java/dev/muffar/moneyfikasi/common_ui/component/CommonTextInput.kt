package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun CommonTextInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isClickable : Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onClick: () -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val disableTextColor = if (isClickable) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.outline.copy(0.8f)
    Column(
        modifier = modifier
    ) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClick()
                },
            value = value,
            onValueChange = onValueChange,
            shape = MaterialTheme.shapes.large,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.8f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.8f),
                focusedPlaceholderColor = MaterialTheme.colorScheme.outline.copy(0.8f),
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline.copy(0.8f),
                disabledContainerColor = if (isClickable) MaterialTheme.colorScheme.surfaceVariant.copy(0.8f) else Color.Transparent,
                disabledTextColor = if (isClickable) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.outline.copy(0.8f),
                disabledBorderColor = if (isClickable) MaterialTheme.colorScheme.surfaceVariant.copy(0.8f) else MaterialTheme.colorScheme.outline.copy(0.8f),
                disabledLeadingIconColor = disableTextColor,
                disabledTrailingIconColor = disableTextColor
            ),
            placeholder = {
                Text(text = placeholder)
            },
            enabled = if (isClickable) false else enabled,
            readOnly = if (isClickable) true else readOnly,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            leadingIcon = leadingIcon
        )
    }
}

@Composable
fun CommonTextInput(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String? = null,
    placeholder: String,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    Column(
        modifier = modifier
    ) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            shape = MaterialTheme.shapes.large,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.8f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.8f),
                focusedPlaceholderColor = MaterialTheme.colorScheme.outline.copy(0.8f),
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline.copy(0.8f)
            ),
            placeholder = {
                Text(text = placeholder)
            },
            enabled = enabled,
            readOnly = readOnly,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
        )
    }
}
