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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.ColorFieldButton
import dev.muffar.moneyfikasi.common_ui.component.CommonTextInput
import dev.muffar.moneyfikasi.common_ui.component.IconFieldButton
import dev.muffar.moneyfikasi.resource.R
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
            value = balance,
            onValueChange = { value ->
                if (value.isEmpty()) {
                    onBalanceChange("0")
                    return@CommonTextInput
                }

                val filtered = value.filter { it.isDigit() }
                val formatted = if (balance == "0") filtered.removePrefix("0") else filtered
                onBalanceChange(formatted)
            },
            label = stringResource(R.string.balance),
            placeholder = stringResource(R.string.enter_wallet_balance),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            )
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