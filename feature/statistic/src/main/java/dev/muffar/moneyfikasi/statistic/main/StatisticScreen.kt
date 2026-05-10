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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.statistic.main.component.IncomeExpenseTrendSection
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.ChooseDateSheet
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.CustomDateSheet
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.DateRangeSwitcher
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.statistic.main.component.StatisticByCategory
import dev.muffar.moneyfikasi.statistic.main.component.StatisticOverviewSection
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
    onShowChooseDateSheet: (Boolean) -> Unit,
    onShowCustomDateSheet: (Boolean) -> Unit,
) {
    Scaffold(
        topBar = { StatisticTopBar(onFilterClick = { onShowChooseDateSheet(true) }) },
        contentWindowInsets = WindowInsets(0.dp),
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            DateRangeSwitcher(
                timeReference = state.timeReference,
                dateRange = state.dateRange,
                onTimeReferenceChange = onTimeReferenceChange,
            )

            StatisticOverviewSection(
                income = state.overviewIncome,
                expense = state.overviewExpense,
                total = state.overviewNet
            )

            IncomeExpenseTrendSection(
                incomeTransactions = state.incomeTransactions,
                expenseTransactions = state.expenseTransactions,
                dateRange = state.dateRange
            )

            StatisticByCategory(
                transactionsIncome = state.incomeTransactions,
                transactionsExpense = state.expenseTransactions,
                onItemClick = { category ->
                    onItemClick(
                        state.dateRange.start to state.dateRange.end,
                        category.id,
                        category.name
                    )
                }
            )
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