package dev.muffar.moneyfikasi.statistic.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.ChooseDateSheet
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.CustomDateSheet
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.DateRangeSwitcher
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.statistic.main.component.CategoryDistributionSection
import dev.muffar.moneyfikasi.statistic.main.component.IncomeExpenseTrendSection
import dev.muffar.moneyfikasi.statistic.main.component.StatisticInsightsSection
import dev.muffar.moneyfikasi.statistic.main.component.StatisticSummarySection
import dev.muffar.moneyfikasi.statistic.main.component.StatisticTopBar
import org.threeten.bp.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatisticScreen(
    modifier: Modifier = Modifier,
    state: StatisticState,
    onTimeReferenceChange: (LocalDateTime) -> Unit,
    onDateRangeChange: (DateRange) -> Unit,
    onItemClick: (dateRange: Pair<Long, Long>, category: UUID, categoryName: String) -> Unit,
    onShowAllClick: () -> Unit,
    onShowChooseDateSheet: (Boolean) -> Unit,
    onShowCustomDateSheet: (Boolean) -> Unit,
) {
    var isChartSliding by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { StatisticTopBar(onFilterClick = { onShowChooseDateSheet(true) }) },
        contentWindowInsets = WindowInsets(0.dp),
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier
                .padding(it)
                .padding(bottom = 80.dp)
        ) {
            DateRangeSwitcher(
                timeReference = state.timeReference,
                dateRange = state.dateRange,
                onTimeReferenceChange = onTimeReferenceChange,
            )
            Column(
                modifier = Modifier.verticalScroll(scrollState, enabled = !isChartSliding)
            ) {
                StatisticSummarySection(
                    income = state.incomeSum,
                    expense = state.expenseSum,
                    net = state.netSum
                )

                IncomeExpenseTrendSection(
                    trend = state.trend,
                    timePeriod = state.dateRange.timePeriod,
                    onSliding = { sliding ->
                        isChartSliding = sliding
                    }
                )

                CategoryDistributionSection(
                    categoryStatistics = state.categoryStatistics,
                    onItemClick = { category ->
                        onItemClick(
                            state.dateRange.start to state.dateRange.end,
                            category.id,
                            category.name
                        )
                    },
                    onShowAllClick = onShowAllClick
                )

                StatisticInsightsSection(
                    highestExpense = state.statisticInsight.highestExpense,
                    highestIncome = state.statisticInsight.highestIncome,
                    mostFreqExpenseCategory = state.statisticInsight.mostFrequentExpenseCategory,
                    mostFreqIncomeCategory = state.statisticInsight.mostFrequentIncomeCategory,
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
    }
}
