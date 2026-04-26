package dev.muffar.moneyfikasi.budget.add_edit.component

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
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.StringExt.filterAmount

@Composable
fun BudgetAmountInput(
    amount: String,
    error: ErrorMessage,
    onAmountChange: (String) -> Unit
) {
    CommonTextInput(
        modifier = Modifier.fillMaxWidth(),
        value = TextFieldValue(amount, TextRange(amount.length)),
        onValueChange = { it.text.filterAmount()?.let(onAmountChange) },
        label = stringResource(R.string.amount),
        placeholder = stringResource(R.string.enter_amount),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        error = error
    )
}