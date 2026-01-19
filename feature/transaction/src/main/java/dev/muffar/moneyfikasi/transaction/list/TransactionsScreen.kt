package dev.muffar.moneyfikasi.transaction.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.ChooseDateSheet
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.CustomDateSheet
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.DateRangeSwitcher
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TransactionFilter
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.list.component.ExpandableTransactionButton
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsFilterSheet
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsList
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsLoading
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsTopBar
import org.threeten.bp.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    state: TransactionsState,
    onTransactionItemClick: (UUID, Boolean) -> Unit,
    onAddTransactionClick: (TransactionType?) -> Unit,
    onTimeReferenceChange: (LocalDateTime) -> Unit,
    onDateRangeChange: (DateRange) -> Unit,
    onShowFilterSheet: (Boolean) -> Unit,
    onShowChooseDateSheet: (Boolean) -> Unit,
    onShowCustomDateSheet: (Boolean) -> Unit,
    onResetFilter: () -> Unit,
    onFilterChanged: (TransactionFilter) -> Unit,
) {
    Scaffold(
        topBar = {
            TransactionsTopBar(
                onChooseDateClick = { onShowChooseDateSheet(true) },
                showFilterBadge = state.isFilterApplied,
                onFilterClick = { onShowFilterSheet(true) }
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
        floatingActionButton = {
            ExpandableTransactionButton(
                onIncomeClick = { onAddTransactionClick(TransactionType.INCOME) },
                onExpenseClick = { onAddTransactionClick(TransactionType.EXPENSE) },
                onTransferClick = { onAddTransactionClick(null) },
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            DateRangeSwitcher(
                timeReference = state.timeReference,
                dateRange = state.dateRange,
                onTimeReferenceChange = onTimeReferenceChange,
            )

            when {
                state.transactionsByDate.isNotEmpty() -> TransactionsList(
                    transactionsByDate = state.transactionsByDate,
                    onItemClick = onTransactionItemClick
                )

                state.isLoading -> TransactionsLoading()

                else -> EmptyDataList(
                    painter = painterResource(id = R.drawable.ic_empty_transactions),
                    title = stringResource(id = R.string.no_transactions),
                    description = stringResource(id = R.string.no_transactions_message)
                )
            }
        }

        AnimatedVisibility(state.showChooseDateSheet) {
            ChooseDateSheet(
                dateRange = state.dateRange,
                onDismissRequest = { onShowChooseDateSheet(false) },
                onCustomDateClick = { onShowCustomDateSheet(true) },
                onChoose = { dateRange ->
                    onDateRangeChange(dateRange)
                    onShowChooseDateSheet(false)
                }
            )
        }

        AnimatedVisibility(state.showCustomDateSheet) {
            CustomDateSheet(
                dateRange = state.dateRange,
                onDateChange = { dateRange ->
                    onDateRangeChange(dateRange)
                    onShowCustomDateSheet(false)
                },
                onDismissRequest = { onShowCustomDateSheet(false) }
            )
        }

        AnimatedVisibility(state.showFilterSheet) {
            TransactionsFilterSheet(
                filter = state.filter,
                isFilterApplied = state.isFilterApplied,
                categories = state.categories,
                wallets = state.wallets,
                onApply = onFilterChanged,
                onResetFilter = onResetFilter,
                onDismissRequest = { onShowFilterSheet(false) }
            )
        }
    }
}