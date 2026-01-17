package dev.muffar.moneyfikasi.transaction.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.utils.TransactionDateFilter
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.list.component.TransactionFloatingActionButton
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsDateFilterSection
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsFilterSheet
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsList
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsLoading
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsTopBar
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    state: TransactionsState,
    onTransactionItemClick: (UUID) -> Unit,
    onFloatingActionButtonClick: (Boolean) -> Unit,
    onAddTransactionClick: (TransactionType?) -> Unit,
    onFilterChange: (TransactionDateFilter) -> Unit,
    onLocalDateTimeChange: (LocalDateTime) -> Unit,
    onDateRangeChange: (Long, Long) -> Unit,
    onShowFilterSheet: (Boolean) -> Unit,
    onFilterCategories: (Set<Category>) -> Unit,
    onFilterWallets: (Set<Wallet>) -> Unit,
) {
    val filterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val scope = rememberCoroutineScope()
    val hideFilterSheet = {
        onShowFilterSheet(false)
        scope.launch { filterSheetState.hide() }
    }

    Scaffold(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { onFloatingActionButtonClick(false) })
        },
        topBar = {
            TransactionsTopBar(
                showFilterBadge = state.isCategoryFiltered || state.isWalletFiltered,
                onFilterClick = { onShowFilterSheet(true) }
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
        floatingActionButton = {
            TransactionFloatingActionButton(
                isExpanded = state.isExpandedFab,
                onClick = { onFloatingActionButtonClick(!state.isExpandedFab) },
                onTransactionClick = {
                    onAddTransactionClick(it)
                    onFloatingActionButtonClick(false)
                },
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            TransactionsDateFilterSection(
                filter = state.filter,
                currentLocalDateTime = state.currentLocalDateTime,
                startDateMillis = state.startDateRange,
                endDateMillis = state.endDateRange,
                onLocalDateTimeChange = onLocalDateTimeChange,
                onDateChange = onDateRangeChange
            )

            when {
                state.transactionsByDate.isNotEmpty() -> TransactionsList(
                    transactionsByDate = state.transactionsByDate,
                    onItemClick = onTransactionItemClick
                )

                state.isLoading -> TransactionsLoading()

                else -> EmptyDataList(
                    painter = painterResource(id = R.drawable.ic_empty_transactions),
                    description = stringResource(id = R.string.no_transactions)
                )
            }
        }
        AnimatedVisibility(state.showFilterSheet) {
            ModalBottomSheet(
                modifier = Modifier.statusBarsPadding(),
                onDismissRequest = { hideFilterSheet() },
                sheetState = filterSheetState,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                TransactionsFilterSheet(
                    filter = state.filter,
                    categories = state.categories,
                    isCategoryFiltered = state.isCategoryFiltered,
                    wallets = state.wallets,
                    isWalletFiltered = state.isWalletFiltered,
                    selectedCategories = state.selectedCategories,
                    selectedWallets = state.selectedWallets,
                    startDateMillis = state.startDateRange,
                    endDateMillis = state.endDateRange,
                    onApply = { filter, startDate, endDate, categories, wallets ->
                        onFilterChange(filter)
                        if (filter == TransactionDateFilter.CUSTOM) {
                            onDateRangeChange(startDate, endDate)
                        }
                        onFilterCategories(categories)
                        onFilterWallets(wallets)
                        hideFilterSheet()
                    },
                    onClose = { hideFilterSheet() }
                )
            }
        }

    }
}