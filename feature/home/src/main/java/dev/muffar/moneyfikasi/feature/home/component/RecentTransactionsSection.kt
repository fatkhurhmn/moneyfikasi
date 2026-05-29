package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.transaction.item.TransactionItem
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun RecentTransactionsSection(
    transactions: List<Transaction>,
    onSeeAllTransactionsClick: () -> Unit,
    onTransactionClick: (UUID, Boolean) -> Unit,
) {
    if (transactions.isEmpty()) return
    Column {
        DashboardLabel(
            label = stringResource(R.string.title_recent_transactions),
            onMoreClick = onSeeAllTransactionsClick
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 4.dp)
        ) {
            transactions.forEachIndexed { index, it ->
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
                if (index < transactions.size - 1) {
                    CommonHorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
