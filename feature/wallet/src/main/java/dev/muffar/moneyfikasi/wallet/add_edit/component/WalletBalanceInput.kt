package dev.muffar.moneyfikasi.wallet.add_edit.component

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
import dev.muffar.moneyfikasi.utils.extensions.StringExt.filterAmount
import java.util.UUID

@Composable
fun WalletBalanceInput(
    id: UUID? = null,
    balance: String,
    onBalanceChange: (String) -> Unit
) {
    CommonTextInput(
        modifier = Modifier.fillMaxWidth(),
        value = TextFieldValue(balance, TextRange(balance.length)),
        onValueChange = { it.text.filterAmount()?.let(onBalanceChange) },
        label = stringResource(R.string.balance),
        placeholder = stringResource(R.string.enter_wallet_balance),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        enabled = id == null,
        readOnly = id != null
    )
}