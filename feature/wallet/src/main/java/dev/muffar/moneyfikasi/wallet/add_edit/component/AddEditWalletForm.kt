package dev.muffar.moneyfikasi.wallet.add_edit.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.ColorFieldButton
import dev.muffar.moneyfikasi.common_ui.component.CommonTextInput
import dev.muffar.moneyfikasi.common_ui.component.IconFieldButton
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.clearThousandFormat
import dev.muffar.moneyfikasi.utils.formatThousand
import java.util.UUID

@Composable
fun AddEditWalletForm(
    modifier: Modifier = Modifier,
    id: UUID?,
    name: String,
    balance: String,
    icon: String,
    color: Long,
    isActive: Boolean,
    onNameChange: (String) -> Unit,
    onBalanceChange: (String) -> Unit,
    onIconClick: () -> Unit,
    onColorClick: () -> Unit,
    onIsActiveChange: () -> Unit,
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        CommonTextInput(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = onNameChange,
            label = stringResource(R.string.name),
            placeholder = stringResource(R.string.enter_wallet_name),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        CommonTextInput(
            modifier = Modifier.fillMaxWidth(),
            value = TextFieldValue(balance, TextRange(balance.length)),
            onValueChange = { newInput ->
                if (newInput.text.length > 20) return@CommonTextInput

                val filtered = newInput.text.filter { it.isDigit() }
                val parsedValue = if (filtered.isNotBlank()) {
                    filtered.clearThousandFormat().toLong().formatThousand()
                } else {
                    "0"
                }

                val newText = newInput.copy(text = parsedValue)
                onBalanceChange(newText.text)
            },
            label = stringResource(R.string.balance),
            placeholder = stringResource(R.string.enter_wallet_balance),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            enabled = id == null,
            readOnly = id != null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
        ) {
            IconFieldButton(
                icon = icon,
                color = color,
                onIconClick = onIconClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            ColorFieldButton(
                color = color,
                onColorClick = onColorClick,
                modifier = Modifier.weight(1f)
            )
        }

        if (id != null) {
            Spacer(modifier = Modifier.height(16.dp))
            WalletActivationButton(
                isActive = isActive,
                onIsActiveChange = onIsActiveChange
            )
        }
    }
}