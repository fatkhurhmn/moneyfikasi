package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.keyboardAsState
import dev.muffar.moneyfikasi.domain.model.ErrorMessage

@Composable
fun CommonTextInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: ErrorMessage = ErrorMessage(),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isClickable: Boolean = false,
    maxLines: Int = 1,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onClick: () -> Unit = {},
    onClear: (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val isKeyboardVisible by keyboardAsState()
    val focusManager = LocalFocusManager.current
    var isFocus by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it.trimStart()) },
            enabled = enabled && !isClickable,
            readOnly = readOnly || isClickable,
            singleLine = maxLines == 1,
            maxLines = maxLines,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            textStyle = textStyle.copy(
                color = MaterialTheme.colorScheme.onSurface,
            ),
            decorationBox = { innerTextField ->
                TextInputDecoration(
                    isFocus = isFocus,
                    label = label,
                    error = error,
                    enabled = enabled,
                    isEmpty = value.isEmpty(),
                    leadingIcon = leadingIcon,
                    onClear = onClear,
                    innerTextField = innerTextField
                )
            },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocus = it.isFocused }
                .clickable(
                    enabled = isClickable,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() }
        )

        TextInputError(error)
    }

    LaunchedEffect(isKeyboardVisible) {
        if (!isKeyboardVisible) {
            focusManager.clearFocus()
        }
    }
}

@Composable
fun CommonTextInput(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    error: ErrorMessage = ErrorMessage(),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onClear: (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val isKeyboardVisible by keyboardAsState()
    val focusManager = LocalFocusManager.current
    var isFocus by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = true,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocus = it.isFocused },
            decorationBox = { innerTextField ->
                TextInputDecoration(
                    isFocus = isFocus,
                    label = label,
                    error = error,
                    enabled = enabled,
                    isEmpty = value.text.isEmpty(),
                    leadingIcon = leadingIcon,
                    onClear = onClear,
                    innerTextField = innerTextField
                )
            },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
        )

        TextInputError(error)
    }

    LaunchedEffect(isKeyboardVisible) {
        if (!isKeyboardVisible) {
            focusManager.clearFocus()
        }
    }
}
