package dev.muffar.moneyfikasi.statistic.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.transaction.TransactionsList
import dev.muffar.moneyfikasi.statistic.detail.component.TotalAmount
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime
import java.util.UUID

@Composable
fun StatisticDetailScreen(
    categoryName: String,
    state: StatisticDetailState,
    onClick: (UUID, Boolean) -> Unit,
    onBackClick: () -> Unit,
    onGetDailySum: (LocalDateTime) -> Flow<Double>,
) {
    val transactions = state.transactions.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = categoryName,
                onBackClick = onBackClick
            )
        }
    ) {
        TransactionsList(
            header = {
                TotalAmount(
                    type = state.type,
                    totalAmount = state.totalAmount
                )
            },
            modifier = Modifier.padding(it),
            transactions = transactions,
            onItemClick = onClick,
            onGetDailyBalance = onGetDailySum
        )
    }
}
