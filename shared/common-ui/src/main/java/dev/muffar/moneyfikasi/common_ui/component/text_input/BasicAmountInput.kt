package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.StringExt.filterAmount

@Composable
fun BasicAmountInput(
    amount: String,
    onAmountChange: (String) -> Unit,
    label: String = stringResource(R.string.label_amount),
    error: ErrorMessage = ErrorMessage(),
    imeAction: ImeAction = ImeAction.Done,
    enabled: Boolean = true
) {
    CommonTextInput(
        modifier = Modifier.fillMaxWidth(),
        value = TextFieldValue(amount, TextRange(amount.length)),
        onValueChange = { it.text.filterAmount()?.let(onAmountChange) },
        label = label,
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = KeyboardType.Number
        ),
        error = error,
        enabled = enabled
    )
}
