package dev.muffar.moneyfikasi.transaction.transfer.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.IconFieldButton
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.transfer.TransferTransactionState
import dev.muffar.moneyfikasi.utils.extensions.filterAmount
import dev.muffar.moneyfikasi.utils.extensions.toFormattedDateTime
import java.util.Locale

@Composable
fun TransferTransactionForm(
    state: TransferTransactionState,
    onAmountChange: (String) -> Unit,
    onOriginWalletClick: () -> Unit,
    onDestinationWalletClick: () -> Unit,
    onAdminFeeChange: (String) -> Unit,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    onTransferClick: () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CommonTextInput(
                modifier = Modifier.weight(1f),
                value = state.sourceWallet.name,
                error = state.sourceWalletError,
                onValueChange = {},
                label = stringResource(R.string.from),
                placeholder = stringResource(R.string.select_wallet),
                isClickable = true,
                onClick = onOriginWalletClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconFieldButton(
                icon = state.sourceWallet.icon,
                color = state.sourceWallet.color,
                showLabel = false,
                onIconClick = onOriginWalletClick
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CommonTextInput(
                modifier = Modifier.weight(1f),
                value = state.targetWallet.name,
                error = state.targetWalletError,
                onValueChange = {},
                label = stringResource(R.string.to),
                placeholder = stringResource(R.string.select_wallet),
                isClickable = true,
                onClick = onDestinationWalletClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconFieldButton(
                icon = state.targetWallet.icon,
                color = state.targetWallet.color,
                showLabel = false,
                onIconClick = onDestinationWalletClick
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        CommonTextInput(
            modifier = Modifier.fillMaxWidth(),
            value = TextFieldValue(state.amount, TextRange(state.amount.length)),
            error = state.amountError,
            onValueChange = { it.text.filterAmount()?.let(onAmountChange) },
            label = stringResource(R.string.amount),
            placeholder = stringResource(R.string.enter_amount),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        CommonTextInput(
            modifier = Modifier.fillMaxWidth(),
            value = TextFieldValue(state.fee, TextRange(state.fee.length)),
            onValueChange = { it.text.filterAmount()?.let(onAdminFeeChange) },
            label = stringResource(R.string.admin_fee),
            placeholder = stringResource(R.string.enter_admin_fee),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            CommonTextInput(
                modifier = Modifier.weight(0.6f),
                value = state.date.toFormattedDateTime("MMM, dd yyyy"),
                onValueChange = {},
                label = stringResource(R.string.date),
                placeholder = stringResource(R.string.select_date),
                isClickable = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.CalendarToday,
                        contentDescription = stringResource(R.string.select_date),
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = onDateClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            CommonTextInput(
                modifier = Modifier.weight(0.4f),
                value = String.format(Locale.getDefault(), "%02d:%02d", state.hour, state.minute),
                onValueChange = {},
                label = stringResource(R.string.time),
                placeholder = stringResource(R.string.select_time),
                isClickable = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Schedule,
                        contentDescription = stringResource(R.string.select_time),
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = onTimeClick
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        TransferTransactionButton(onTransferClick)
    }
}