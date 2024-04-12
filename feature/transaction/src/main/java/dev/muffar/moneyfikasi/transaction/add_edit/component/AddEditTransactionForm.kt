package dev.muffar.moneyfikasi.transaction.add_edit.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.clearThousandFormat
import dev.muffar.moneyfikasi.utils.formatThousand

@Composable
fun AddEditTransactionForm(
    modifier: Modifier = Modifier,
    amount: String,
    description: String,
    categoryName: String,
    categoryIcon: String,
    categoryColor: Long,
    walletName: String,
    walletIcon: String,
    walletColor: Long,
    date: String,
    time: String,
    onAmountChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategoryClick: () -> Unit,
    onWalletClick: () -> Unit,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CommonTextInput(
            modifier = Modifier.fillMaxWidth(),
            value = TextFieldValue(amount, TextRange(amount.length)),
            onValueChange = { newInput ->
                if (newInput.text.length > 20) return@CommonTextInput

                val filtered = newInput.text.filter { it.isDigit() }
                val parsedValue = if (filtered.isNotBlank()) {
                    filtered.clearThousandFormat().toLong().formatThousand()
                } else {
                    "0"
                }

                val newText = newInput.copy(text = parsedValue)
                onAmountChange(newText.text)
            },
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
            value = description,
            onValueChange = onDescriptionChange,
            label = stringResource(R.string.description),
            placeholder = stringResource(R.string.enter_description),
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
                value = categoryName,
                onValueChange = {},
                label = stringResource(R.string.category),
                placeholder = stringResource(R.string.select_category),
                isClickable = true,
                onClick = onCategoryClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconFieldButton(
                icon = categoryIcon,
                color = categoryColor,
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
                value = walletName,
                onValueChange = {},
                label = stringResource(R.string.wallet),
                placeholder = stringResource(R.string.select_wallet),
                isClickable = true,
                onClick = onWalletClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconFieldButton(
                icon = walletIcon,
                color = walletColor,
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