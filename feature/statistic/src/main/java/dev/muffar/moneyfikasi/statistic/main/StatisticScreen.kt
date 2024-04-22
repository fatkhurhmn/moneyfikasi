package dev.muffar.moneyfikasi.statistic.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.utils.TransactionFilter
import dev.muffar.moneyfikasi.statistic.main.component.StatisticBottomSheet
import dev.muffar.moneyfikasi.statistic.main.component.StatisticDateFilterSection
import dev.muffar.moneyfikasi.statistic.main.component.StatisticOverviewSection
import dev.muffar.moneyfikasi.statistic.main.component.StatisticSheetType
import dev.muffar.moneyfikasi.statistic.main.component.StatisticTopBar

@Composable
fun StatisticScreen(
    modifier: Modifier = Modifier,
    state: StatisticState,
    onFilterChanged: (TransactionFilter) -> Unit,
    onDateRangeChange: (Long, Long) -> Unit,
    onShowBottomSheet: (StatisticSheetType?) -> Unit,
) {
    Scaffold(
        topBar = {
            StatisticTopBar(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onFilterClick = { onShowBottomSheet(StatisticSheetType.FILTER) }
            )
        },
    ) {
        Column(
            modifier = modifier.padding(it)
        ) {
            StatisticDateFilterSection(
                filter = state.filter,
                startDateMillis = state.startDateRange,
                endDateMillis = state.endDateRange,
                onDateChange = onDateRangeChange
            )

            StatisticOverviewSection(
                income = state.overviewIncome,
                expense = state.overviewExpense,
                total = state.overviewTotal
            )
        }

        if (state.sheetType != null) {
            StatisticBottomSheet(
                type = state.sheetType,
                filter = state.filter,
                startDateMillis = state.startDateRange,
                endDateMillis = state.endDateRange,
                onFilterChanged = onFilterChanged,
                onDateChange = { start, date ->
                    onDateRangeChange(start, date)
                    onFilterChanged(TransactionFilter.CUSTOM)
                },
                onShowBottomSheet = onShowBottomSheet
            )
        }
    }
}