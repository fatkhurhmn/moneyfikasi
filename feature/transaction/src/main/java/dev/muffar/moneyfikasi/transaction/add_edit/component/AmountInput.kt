package dev.muffar.moneyfikasi.transaction.add_edit.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.filterAmount

@Composable
fun AmountInput(
    value: String,
    onValueChange: (String) -> Unit
) {
    CommonTextInput(
        modifier = Modifier.fillMaxWidth(),
        value = TextFieldValue(value, TextRange(value.length)),
        onValueChange = { it.text.filterAmount()?.let(onValueChange) },
        label = stringResource(R.string.amount),
        placeholder = stringResource(R.string.enter_amount),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        )
    )
}