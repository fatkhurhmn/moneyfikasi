package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TransactionsTopBar(
    totalBalance: Double,
    isBalanceVisible: Boolean,
    showFilterBadge: Boolean,
    onVisibilityClick: () -> Unit,
    onFilterClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TotalBalance(
            modifier = Modifier
                .weight(0.75f)
                .padding(end = 32.dp),
            totalBalance = totalBalance,
            isBalanceVisible = isBalanceVisible,
            onVisibilityClick = onVisibilityClick,
        )
        FilterIcon(
            isFilterApplied = showFilterBadge,
            onFilterClick = onFilterClick,
        )
    }
}