package dev.muffar.moneyfikasi.feature.dashboard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.DateRange

@Composable
fun ReportCard(
    dateRange: DateRange,
    balance: Double,
    income: Double,
    expense: Double,
    onDateRangeClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ReportLabel(
            dateRange = dateRange,
            onDateRangeClick = onDateRangeClick
        )
        OverviewBalance(balance = balance)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OverviewIncomeExpense(
                modifier = Modifier.weight(1f),
                categoryType = CategoryType.INCOME,
                amount = income
            )
            Spacer(modifier = Modifier.width(8.dp))
            OverviewIncomeExpense(
                modifier = Modifier.weight(1f),
                categoryType = CategoryType.EXPENSE,
                amount = expense
            )
        }
    }
}