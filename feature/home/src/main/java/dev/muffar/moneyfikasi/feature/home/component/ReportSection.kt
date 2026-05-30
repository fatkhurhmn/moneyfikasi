package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.feature.home.HomeState

@Composable
fun ReportSection(
    state: HomeState,
    onDateRangeClick: () -> Unit,
    onVisibilityClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ReportLabel(
            dateRange = state.dateRange,
            onDateRangeClick = onDateRangeClick,
        )
        OverviewNet(
            net = state.reportNet,
            trendResult = state.balanceTrend,
            timePeriod = state.dateRange.timePeriod,
            isBalanceVisible = state.isReportVisible,
            onVisibilityClick = onVisibilityClick,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OverviewIncomeExpense(
                modifier = Modifier.weight(1f),
                categoryType = CategoryType.INCOME,
                amount = state.reportIncome,
                isAmountVisible = state.isReportVisible,
            )
            Spacer(modifier = Modifier.width(8.dp))
            OverviewIncomeExpense(
                modifier = Modifier.weight(1f),
                categoryType = CategoryType.EXPENSE,
                amount = state.reportExpense,
                isAmountVisible = state.isReportVisible,
            )
        }
    }
}