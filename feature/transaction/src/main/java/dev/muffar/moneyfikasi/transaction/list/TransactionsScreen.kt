package dev.muffar.moneyfikasi.transaction.list

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.ExpandableFloatingActionButton
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.utils.TransactionDateFilter
import dev.muffar.moneyfikasi.transaction.list.component.EmptyTransactions
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsDateFilterSection
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsFilterSheet
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsList
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsLoading
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsTopBar
import org.threeten.bp.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    state: TransactionsState,
    onTransactionItemClick: (UUID) -> Unit,
    onExpandFabButton: (Boolean) -> Unit,
    onNavigateToAddScreen: (TransactionType) -> Unit,
    onFilterChanged: (TransactionDateFilter) -> Unit,
    onLocalDateTimeChange: (LocalDateTime) -> Unit,
    onDateRangeChange: (Long, Long) -> Unit,
    onVisibilityClick: () -> Unit,
    onShowFilterSheet: (Boolean) -> Unit,
    onFilterCategories: (Set<Category>) -> Unit,
    onFilterWallets: (Set<Wallet>) -> Unit,
    onSaveFilter: () -> Unit,
) {
    val filterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    onExpandFabButton(false)
                }
            )
        },
        topBar = {
            TransactionsTopBar(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                totalBalance = state.totalBalance,
                isBalanceVisible = state.isBalanceVisible,
                onVisibilityClick = onVisibilityClick,
                onFilterClick = { onShowFilterSheet(true) }
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
        floatingActionButton = {
            ExpandableFloatingActionButton(
                isExpanded = state.isExpandedFab,
                onClick = { onExpandFabButton(!state.isExpandedFab) },
                onTransactionClick = {
                    onNavigateToAddScreen(it)
                    onExpandFabButton(false)
                }
            )
        }
    ) {
        Column(
            modifier = modifier.padding(it)
        ) {
            TransactionsDateFilterSection(
                filter = state.filter,
                currentLocalDateTime = state.currentLocalDateTime,
                startDateMillis = state.startDateRange,
                endDateMillis = state.endDateRange,
                onLocalDateTimeChange = onLocalDateTimeChange,
                onDateChange = onDateRangeChange
            )

            if (state.transactionsByDate.isNotEmpty()) {
                val dates = state.transactionsByDate.keys.toList()
                val transactions = state.transactionsByDate.values.toList()

                TransactionsList(
                    dates = dates,
                    transactions = transactions,
                    overviewIncome = state.overviewIncome,
                    overviewExpense = state.overviewExpense,
                    overviewTotal = state.overviewTotal,
                    onItemClick = onTransactionItemClick
                )
            } else {
                if (state.isLoading) {
                    TransactionsLoading()
                } else {
                    EmptyTransactions()
                }
            }

            if (state.showFilterSheet) {
                ModalBottomSheet(
                    onDismissRequest = { onShowFilterSheet(false) },
                    sheetState = filterSheetState
                ) {
                    TransactionsFilterSheet(
                        filter = state.filter,
                        categories = state.categories,
                        wallets = state.wallets,
                        selectedCategories = state.selectedCategories,
                        selectedWallets = state.selectedWallets,
                        startDateMillis = state.startDateRange,
                        endDateMillis = state.endDateRange,
                        onSave = { filter, startDate, endDate, categories, wallets ->
                            onFilterChanged(filter)
                            if (filter == TransactionDateFilter.CUSTOM) {
                                onDateRangeChange(startDate, endDate)
                            }
                            onFilterCategories(categories)
                            onFilterWallets(wallets)
                            onSaveFilter()
                        },
                        onClose = {
                            onShowFilterSheet(false)
                        }
                    )
                }
            }
        }
    }
}