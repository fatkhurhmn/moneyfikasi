package dev.muffar.moneyfikasi.transaction.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.ChooseDateSheet
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.CustomDateSheet
import dev.muffar.moneyfikasi.common_ui.component.calendar.CalendarDayCell
import dev.muffar.moneyfikasi.common_ui.component.calendar.WeekdayHeader
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.CalendarHeader
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.DateRangeSwitcher
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.transaction.TransactionsList
import dev.muffar.moneyfikasi.common_ui.component.transaction.item.TransactionItem
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TransactionFilter
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsFilterSheet
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsLoading
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsTopBar
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.format
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
    val transactions = state.transactions.collectAsLazyPagingItems()

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
                // Calendar header - bigger & proper
                val title =
                    state.calendarMonth.format("MMMM yyyy").replaceFirstChar { c -> c.uppercase() }
                CalendarHeader(
                    title = title,
                    onPreviousClick = onCalendarPreviousMonth,
                    onNextClick = onCalendarNextMonth
                )
                // Weekday header - separate container
                dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard(
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    WeekdayHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
                // Calendar grid - separate container
                PrimaryCard(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    TransactionCalendarGrid(
                        calendarMonth = state.calendarMonth,
                        dailyBalances = state.calendarDailyBalances,
                        selectedDay = state.calendarSelectedDay,
                        onDaySelected = onCalendarDaySelected,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                if (state.calendarSelectedDay != null) {
                    val day = state.calendarSelectedDay
                    val dateTitle = state.calendarMonth.withDayOfMonth(day).format("dd MMMM yyyy")
                    Text(
                        text = "Transaksi $dateTitle",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    if (state.calendarSelectedDayTransactions.isEmpty()) {
                        Text(
                            text = stringResource(R.string.empty_transactions_msg),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    } else {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            state.calendarSelectedDayTransactions.forEach { tx ->
                                TransactionItem(
                                    transaction = tx,
                                    onClick = { onTransactionItemClick(tx.id, tx.isTransfer) }
                                )
                            }
                        }
                    }
                }
            } else {
                DateRangeSwitcher(
                    timeReference = state.timeReference,
                    dateRange = state.dateRange,
                    onTimeReferenceChange = onTimeReferenceChange,
                )

                if (transactions.loadState.refresh is LoadState.Loading) {
                    TransactionsLoading()
                } else if (transactions.itemCount == 0) {
                    EmptyDataList(
                        title = stringResource(id = R.string.empty_transactions_title),
                        description = stringResource(id = R.string.empty_transactions_msg),
                        bottomPadding = true
                    )
                } else {
                    TransactionsList(
                        transactions = transactions,
                        onItemClick = onTransactionItemClick,
                        onGetDailyBalance = onGetDailyBalance,
                        extraBottomSpace = true
                    )
                }
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

@Composable
private fun TransactionCalendarGrid(
    calendarMonth: LocalDateTime,
    dailyBalances: Map<Int, Double>,
    selectedDay: Int?,
    onDaySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysInMonth = calendarMonth.toLocalDate().lengthOfMonth()
    val firstDay = calendarMonth.withDayOfMonth(1)
    val firstDow = firstDay.dayOfWeek.value
    val offset = firstDow - 1
    val today = LocalDateTime.now()
    val isCurrentMonthToday =
        today.year == calendarMonth.year && today.monthValue == calendarMonth.monthValue

    val cells = mutableListOf<Int?>()
    repeat(offset) { cells.add(null) }
    for (d in 1..daysInMonth) cells.add(d)
    while (cells.size % 7 != 0) cells.add(null)

    val rows = cells.chunked(7)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                row.forEach { day ->
                    if (day == null) {
                        Box(modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)) {}
                    } else {
                        val balance = dailyBalances[day]
                        val isToday = isCurrentMonthToday && today.dayOfMonth == day
                        Box(modifier = Modifier.weight(1f)) {
                            CalendarDayCell(
                                day = day,
                                balance = balance,
                                isToday = isToday,
                                isSelected = selectedDay == day,
                                isCurrentMonth = true,
                                onClick = { onDaySelected(day) }
                            )
                        }
                    }
                }
            }
        }
    }
}
