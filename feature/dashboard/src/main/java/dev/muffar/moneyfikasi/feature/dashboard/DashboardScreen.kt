package dev.muffar.moneyfikasi.feature.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.feature.dashboard.component.LastTransactionsSection
import dev.muffar.moneyfikasi.feature.dashboard.component.ReportDateSheet
import dev.muffar.moneyfikasi.feature.dashboard.component.ReportSection
import dev.muffar.moneyfikasi.feature.dashboard.component.TotalBalance
import java.util.UUID

@Composable
fun DashboardScreen(
    state: DashboardState,
    onToggleBalanceVisibility: () -> Unit,
    onShowReportDateSheet: (Boolean) -> Unit,
    onDateRangeChange: (DateRange) -> Unit,
    onSeeAllTransactionsClick: () -> Unit,
    onTransactionClick: (UUID, Boolean) -> Unit,
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TotalBalance(
                    totalBalance = state.totalBalance,
                    isBalanceVisible = state.isBalanceVisible,
                    onVisibilityClick = onToggleBalanceVisibility
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            ReportSection(
                state = state,
                onDateRangeClick = { onShowReportDateSheet(true) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LastTransactionsSection(
                transactions = state.lastTransactions,
                onSeeAllTransactionsClick = onSeeAllTransactionsClick,
                onTransactionClick = onTransactionClick
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    AnimatedVisibility(state.showReportDateSheet) {
        ReportDateSheet(
            dateRange = state.dateRange,
            onDismissRequest = { onShowReportDateSheet(false) },
            onChoose = onDateRangeChange
        )
    }
}