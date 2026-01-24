package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.message.ErrorMessage

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
    val disableTextColor =
        if (isClickable) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant
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
            onValueChange = {
                onValueChange(it.trimStart())
            },
            isError = error.message != null,
            shape = MaterialTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                errorBorderColor = MaterialTheme.colorScheme.error,
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

        AnimatedVisibility(
            visible = error.message != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            val shakeAnim = remember { Animatable(0f) }
            LaunchedEffect(error) {
                shakeAnim.animateTo(
                    targetValue = 10f,
                    animationSpec = tween(
                        durationMillis = 50,
                        easing = EaseInOutSine
                    )
                )
                shakeAnim.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 50,
                        easing = EaseInOutSine
                    )
                )
            }
            Text(
                text = error.message ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.offset(x = shakeAnim.value.dp)
            )
        }
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
            shape = MaterialTheme.shapes.medium,
            isError = error.message != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.8f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.8f),
                errorContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.8f),
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            placeholder = { Text(text = placeholder) },
            enabled = enabled,
            readOnly = readOnly,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
        )

        AnimatedVisibility(
            visible = error.message != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            val shakeAnim = remember { Animatable(0f) }
            LaunchedEffect(error) {
                shakeAnim.animateTo(
                    targetValue = 10f,
                    animationSpec = tween(
                        durationMillis = 50,
                        easing = EaseInOutSine
                    )
                )
                shakeAnim.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 50,
                        easing = EaseInOutSine
                    )
                )
            }
            Text(
                text = error.message ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.offset(x = shakeAnim.value.dp)
            )
        }
    }
}
