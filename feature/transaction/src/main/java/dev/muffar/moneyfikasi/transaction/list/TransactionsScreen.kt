package dev.muffar.moneyfikasi.transaction.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.ChooseDateSheet
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.CustomDateSheet
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TransactionFilter
import dev.muffar.moneyfikasi.transaction.list.component.TransactionCalendarSection
import dev.muffar.moneyfikasi.transaction.list.component.TransactionListSection
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsFilterSheet
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsTopBar
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    state: TransactionsState,
    onTransactionItemClick: (UUID, Boolean) -> Unit,
    onTimeReferenceChange: (LocalDateTime) -> Unit,
    onDateRangeChange: (DateRange) -> Unit,
    onShowFilterSheet: (Boolean) -> Unit,
    onShowChooseDateSheet: (Boolean) -> Unit,
    onShowCustomDateSheet: (Boolean) -> Unit,
    onSearchClick: () -> Unit,
    onResetFilter: () -> Unit,
    onFilterChanged: (TransactionFilter) -> Unit,
    onGetDailyBalance: (LocalDateTime) -> Flow<Double>,
    onCalendarToggle: () -> Unit = {},
    onCalendarPreviousMonth: () -> Unit = {},
    onCalendarNextMonth: () -> Unit = {},
    onCalendarDaySelected: (Int) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TransactionsTopBar(
                onChooseDateClick = { onShowChooseDateSheet(true) },
                onSearchClick = onSearchClick,
                showFilterBadge = state.isFilterApplied,
                isCalendarMode = state.isCalendarMode,
                onCalendarToggle = onCalendarToggle,
                onFilterClick = { onShowFilterSheet(true) }
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            if (state.isCalendarMode) {
                TransactionCalendarSection(
                    calendarMonth = state.calendarMonth,
                    dailyBalances = state.calendarDailyBalances,
                    selectedDay = state.calendarSelectedDay,
                    selectedDayTransactions = state.calendarSelectedDayTransactions,
                    onPreviousMonth = onCalendarPreviousMonth,
                    onNextMonth = onCalendarNextMonth,
                    onDaySelected = onCalendarDaySelected,
                    onTransactionClick = onTransactionItemClick
                )
            } else {
                TransactionListSection(
                    timeReference = state.timeReference,
                    dateRange = state.dateRange,
                    transactions = state.transactions,
                    onTimeReferenceChange = onTimeReferenceChange,
                    onTransactionClick = onTransactionItemClick,
                    onGetDailyBalance = onGetDailyBalance
                )
            }
        }

        AnimatedVisibility(state.showChooseDateSheet) {
            ChooseDateSheet(
                dateRange = state.dateRange,
                onDismissRequest = { onShowChooseDateSheet(false) },
                onCustomDateClick = { onShowCustomDateSheet(true) },
                onChoose = onDateRangeChange
            )
        }

        AnimatedVisibility(state.showCustomDateSheet) {
            CustomDateSheet(
                dateRange = state.dateRange,
                onDateChange = onDateRangeChange,
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
