package dev.muffar.moneyfikasi.debt.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun DebtsContent(
    modifier: Modifier = Modifier,
    debts: List<Debt>,
    wallets: List<Wallet>,
    onClick: (UUID) -> Unit,
    onPayClick: (UUID) -> Unit
) {
    if (debts.isNotEmpty()) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(
                top = 8.dp,
                bottom = 80.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(debts, key = { it.id }) { debt ->
                val walletName = wallets.find { it.id == debt.walletId }?.name ?: "Unknown"
                DebtItem(
                    debt = debt,
                    walletName = walletName,
                    onClick = { onClick(debt.id) },
                    onPayClick = { onPayClick(debt.id) }
                )
            }
        }
    } else {
        EmptyDataList(
            title = stringResource(R.string.empty_debts_title),
            description = stringResource(R.string.empty_debts_msg)
        )
    }
}
