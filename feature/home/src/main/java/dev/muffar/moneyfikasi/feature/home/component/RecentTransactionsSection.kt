package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.transaction_item.TransactionItem
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun RecentTransactionsSection(
    transactions: List<Transaction>,
    onSeeAllTransactionsClick: () -> Unit,
    onTransactionClick: (UUID, Boolean) -> Unit,
) {
    Column {
        DashboardLabel(
            label = stringResource(R.string.recent_transaction),
            onSeeAllClick = onSeeAllTransactionsClick
        )
        transactions.forEach {
            TransactionItem(
                transaction = it,
                onClick = { id ->
                    onTransactionClick(
                        id,
                        it.isTransfer || it.category.isFeeTransfer
                    )
                },
                showDate = true
            )
        }
    }
}