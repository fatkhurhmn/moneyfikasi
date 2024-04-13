package dev.muffar.moneyfikasi.transaction.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailBody
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailHeader
import dev.muffar.moneyfikasi.utils.format
import dev.muffar.moneyfikasi.utils.formatThousand

@Composable
fun TransactionDetailScreen(
    modifier: Modifier = Modifier,
    state: TransactionDetailState,
    onBackClick: () -> Unit,
) {
    val amount = state.transaction?.amount?.toLong()?.formatThousand() ?: ""
    val type = state.transaction?.type ?: TransactionType.INCOME
    val wallet = state.transaction?.wallet?.name ?: ""
    val category = state.transaction?.category?.name ?: ""
    val date = state.transaction?.date?.format("dd-MM-yyyy, kk:mm") ?: ""
    val description = state.transaction?.description ?: "-"

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.transaction),
                onBackClick = onBackClick
            )
        },
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .padding(it)
                .fillMaxWidth()
                .background(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.background
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(0.2f),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)
        ) {
            TransactionDetailHeader(amount = amount, type = type)
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(0.2f),
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )
            TransactionDetailBody(
                wallet = wallet,
                category = category,
                type = type.value,
                dateTime = date,
                description = description
            )
        }
    }
}