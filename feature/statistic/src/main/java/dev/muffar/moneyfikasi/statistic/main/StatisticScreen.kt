package dev.muffar.moneyfikasi.statistic.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonTabs
import dev.muffar.moneyfikasi.domain.utils.TransactionDateFilter
import dev.muffar.moneyfikasi.statistic.main.component.StatisticBottomSheet
import dev.muffar.moneyfikasi.statistic.main.component.StatisticDateFilterSection
import dev.muffar.moneyfikasi.statistic.main.component.StatisticOverviewSection
import dev.muffar.moneyfikasi.statistic.main.component.StatisticSheetType
import dev.muffar.moneyfikasi.statistic.main.component.StatisticTopBar
import dev.muffar.moneyfikasi.statistic.main.component.TransactionStatisticContent
import org.threeten.bp.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatisticScreen(
    modifier: Modifier = Modifier,
    state: StatisticState,
    onFilterChanged: (TransactionDateFilter) -> Unit,
    onLocalDateTimeChange: (LocalDateTime) -> Unit,
    onDateRangeChange: (Long, Long) -> Unit,
    onShowBottomSheet: (StatisticSheetType?) -> Unit,
    onItemClick: (dateRange: Pair<Long, Long>, category: UUID) -> Unit,
) {
    val pagerState = rememberPagerState { state.tabs.size }

    Scaffold(
        topBar = {
            StatisticTopBar(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onFilterClick = { onShowBottomSheet(StatisticSheetType.FILTER) }
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
    ) {
        Column(
            modifier = modifier.padding(it)
        ) {
            StatisticDateFilterSection(
                filter = state.filter,
                currentLocalDateTime = state.currentLocalDateTime,
                startDateMillis = state.startDateRange,
                endDateMillis = state.endDateRange,
                onDateChange = onDateRangeChange,
                onLocalDateTimeChange = onLocalDateTimeChange
            )

            StatisticOverviewSection(
                income = state.overviewIncome,
                expense = state.overviewExpense,
                total = state.overviewTotal
            )
            HorizontalDivider(
                thickness = 8.dp,
                color = MaterialTheme.colorScheme.outline.copy(0.08f)
            )
            CommonTabs(
                tabs = state.tabs.map { tab -> tab to false },
                pagerState = pagerState
            ) { index ->
                when (index) {
                    0 -> TransactionStatisticContent(
                        modifier = Modifier,
                        transactions = state.incomeTransactions,
                        onClick = { category ->
                            onItemClick(
                                state.startDateRange to state.endDateRange,
                                category.id
                            )
                        }
                    )

                    1 -> TransactionStatisticContent(
                        modifier = Modifier,
                        transactions = state.expenseTransactions,
                        onClick = { category ->
                            onItemClick(
                                state.startDateRange to state.endDateRange,
                                category.id
                            )
                        }
                    )
                }
            }
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
                    onFilterChanged(TransactionDateFilter.CUSTOM)
                },
                onShowBottomSheet = onShowBottomSheet
            )
        }
    }
}