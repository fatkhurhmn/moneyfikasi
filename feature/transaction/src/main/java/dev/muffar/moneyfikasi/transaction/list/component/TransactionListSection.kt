package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.DateRangeSwitcher
import dev.muffar.moneyfikasi.common_ui.component.transaction.TransactionsList
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime
import java.util.UUID

@Composable
fun TransactionListSection(
    timeReference: LocalDateTime,
    dateRange: DateRange,
    transactions: Flow<PagingData<Transaction>>,
    onTimeReferenceChange: (LocalDateTime) -> Unit,
    onTransactionClick: (UUID, Boolean) -> Unit,
    onGetDailyBalance: (LocalDateTime) -> Flow<Double>,
    modifier: Modifier = Modifier
) {
    val pagingItems = transactions.collectAsLazyPagingItems()

    Column(modifier = modifier) {
        DateRangeSwitcher(
            timeReference = timeReference,
            dateRange = dateRange,
            onTimeReferenceChange = onTimeReferenceChange,
        )

        if (pagingItems.loadState.refresh is LoadState.Loading) {
            TransactionsLoading()
        } else if (pagingItems.itemCount == 0) {
            EmptyDataList(
                title = stringResource(id = R.string.empty_transactions_title),
                description = stringResource(id = R.string.empty_transactions_msg),
                bottomPadding = true
            )
        } else {
            TransactionsList(
                transactions = pagingItems,
                onItemClick = onTransactionClick,
                onGetDailyBalance = onGetDailyBalance,
                extraBottomSpace = true
            )
        }
    }
}
