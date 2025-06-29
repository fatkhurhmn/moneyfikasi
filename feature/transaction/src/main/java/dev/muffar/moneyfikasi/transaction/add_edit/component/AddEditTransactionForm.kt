package dev.muffar.moneyfikasi.transaction.add_edit.component

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
import dev.muffar.moneyfikasi.common_ui.component.CommonTextInput
import dev.muffar.moneyfikasi.common_ui.component.IconFieldButton
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.filterAmount

@Composable
fun AddEditTransactionForm(
    modifier: Modifier = Modifier,
    amount: String,
    note: String,
    category: Category,
    wallet: Wallet,
    date: String,
    time: String,
    onAmountChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onCategoryClick: () -> Unit,
    onWalletClick: () -> Unit,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
) {
    Column {
        CommonTextInput(
            modifier = Modifier.fillMaxWidth(),
            value = TextFieldValue(amount, TextRange(amount.length)),
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
            value = note,
            onValueChange = onNoteChange,
            label = stringResource(R.string.note),
            placeholder = stringResource(R.string.enter_note),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CommonTextInput(
                modifier = Modifier.weight(1f),
                value = category.name,
                onValueChange = {},
                label = stringResource(R.string.category),
                placeholder = stringResource(R.string.select_category),
                isClickable = true,
                onClick = onCategoryClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconFieldButton(
                icon = category.icon,
                color = category.color,
                showLabel = false,
                onIconClick = onCategoryClick
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CommonTextInput(
                modifier = Modifier.weight(1f),
                value = wallet.name,
                onValueChange = {},
                label = stringResource(R.string.wallet),
                placeholder = stringResource(R.string.select_wallet),
                isClickable = true,
                onClick = onWalletClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconFieldButton(
                icon = wallet.icon,
                color = wallet.color,
                showLabel = false,
                onIconClick = onWalletClick
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            CommonTextInput(
                modifier = Modifier.weight(0.6f),
                value = date,
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
                value = time,
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
    }
}