package dev.muffar.moneyfikasi.common_ui.component.text_input

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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import dev.muffar.moneyfikasi.common_ui.component.keyboardAsState
import dev.muffar.moneyfikasi.domain.model.ErrorMessage

@Composable
fun AmountInput(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String? = null,
    error: ErrorMessage = ErrorMessage(),
    availableBalance: String,
) {
    val isKeyboardVisible by keyboardAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isFocus by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
    ) {
        TextInputLabel(label)
        BasicTextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged { isFocus = it.isFocused }
                .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = true,
            readOnly = false,
            keyboardActions = KeyboardActions(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            interactionSource = interactionSource,
            textStyle = MaterialTheme.typography.displaySmall.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                AmountInputDecorationBox(
                    value = value.text,
                    availableBalance = availableBalance,
                    interactionSource = interactionSource,
                    isError = error.message != null,
                    isFocus = isFocus,
                    innerTextField = innerTextField,
                )
            }
        )
        TextInputError(error)
    }


    LaunchedEffect(isKeyboardVisible) {
        if (!isKeyboardVisible) {
            focusManager.clearFocus()
        }
    }
}