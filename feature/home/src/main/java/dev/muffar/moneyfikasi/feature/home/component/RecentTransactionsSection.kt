package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
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

    val visibleTransactions = transactions.take(3)

    Column {
        DashboardLabel(
            label = stringResource(R.string.title_recent_transactions),
            onMoreClick = onSeeAllTransactionsClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            visibleTransactions.forEachIndexed { index, transaction ->

                val shape = when {
                    visibleTransactions.size == 1 -> RoundedCornerShape(16.dp)

                    index == 0 -> RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp
                    )

                    index == visibleTransactions.lastIndex -> RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )

                    else -> RectangleShape
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    TransactionItem(
                        transaction = transaction,
                        onClick = { id ->
                            onTransactionClick(
                                id,
                                transaction.isTransfer || transaction.category.isFeeTransfer
                            )
                        },
                        showDate = true
                    )
                }

                if (index < visibleTransactions.lastIndex) {
                    CommonHorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}