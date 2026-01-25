package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AmountInputDecorationBox(
    value: String,
    availableBalance: String,
    interactionSource: InteractionSource,
    isError: Boolean,
    isFocus: Boolean,
    innerTextField: @Composable (() -> Unit)
) {
    OutlinedTextFieldDefaults.DecorationBox(
        value = value,
        innerTextField = {
            AmountInputInnerTextField(
                value = value,
                availableBalance = availableBalance,
                innerTextField = innerTextField
            )
        },
        enabled = true,
        singleLine = true,
        visualTransformation = VisualTransformation.None,
        interactionSource = interactionSource,
        isError = isError,
        colors = textInputColor(false, isFocus),
        contentPadding = OutlinedTextFieldDefaults.contentPadding(),
        container = {
            AmountInputContainer(
                isError = isError,
                isFocus = isFocus,
                interactionSource = interactionSource
            )
        }
    )
}

@Composable
fun AmountInputInnerTextField(
    value: String,
    availableBalance: String,
    innerTextField: @Composable (() -> Unit)
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (value.isBlank()) {
                Text(
                    text = stringResource(R.string.enter_amount),
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = 0.3f
                    )
                )
            }
            innerTextField()
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = availableBalance,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun AmountInputContainer(
    isError: Boolean,
    isFocus: Boolean,
    interactionSource: InteractionSource,
) {
    OutlinedTextFieldDefaults.Container(
        enabled = true,
        isError = isError,
        interactionSource = interactionSource,
        colors = textInputColor(false, isFocus),
        shape = MaterialTheme.shapes.medium
    )
}