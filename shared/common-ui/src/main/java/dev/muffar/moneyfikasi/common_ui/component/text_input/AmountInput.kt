package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.AmountInputSheet
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.StringExt.clearThousandFormat

@Composable
fun AmountInput(
    amount: String,
    error: ErrorMessage = ErrorMessage(),
    onAmountChange: (String) -> Unit,
    label: String = stringResource(R.string.label_amount),
    onClear: (() -> Unit)? = null
) {
    var showAmountInputSheet by remember { mutableStateOf(false) }
    Column {
        CommonTextInput(
            modifier = Modifier.fillMaxWidth(),
            value = amount,
            onValueChange = { },
            label = label,
            error = error,
            isClickable = true,
            textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            onClick = { showAmountInputSheet = true },
            onClear = onClear
        )

        AnimatedVisibility(showAmountInputSheet) {
            AmountInputSheet(
                amount = amount.clearThousandFormat(),
                onConfirm = {
                    onAmountChange(it.toDouble().formatThousand())
                },
                onDismissRequest = { showAmountInputSheet = false }
            )
        }
    }
}
