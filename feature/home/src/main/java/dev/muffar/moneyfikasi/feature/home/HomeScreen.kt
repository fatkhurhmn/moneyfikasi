package dev.muffar.moneyfikasi.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.feature.home.component.QuickTransactionSection
import dev.muffar.moneyfikasi.feature.home.component.RecentTransactionsSection
import dev.muffar.moneyfikasi.feature.home.component.ReportDateSheet
import dev.muffar.moneyfikasi.feature.home.component.ReportSection
import dev.muffar.moneyfikasi.feature.home.component.TotalBalance
import java.util.UUID

@Composable
fun HomeScreen(
    state: HomeState,
    onToggleBalanceVisibility: () -> Unit,
    onToggleReportVisibility: () -> Unit,
    onShowReportDateSheet: (Boolean) -> Unit,
    onDateRangeChange: (DateRange) -> Unit,
    onSeeAllTransactionsClick: () -> Unit,
    onTransactionClick: (UUID, Boolean) -> Unit,
    onPresetClick: (TransactionType, UUID) -> Unit,
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
                onDateRangeClick = { onShowReportDateSheet(true) },
                onVisibilityClick = onToggleReportVisibility,
            )

            QuickTransactionSection(
                presets = state.presets,
                onPresetClick = onPresetClick,
            )

            RecentTransactionsSection(
                transactions = state.recentTransactions,
                onSeeAllTransactionsClick = onSeeAllTransactionsClick,
                onTransactionClick = onTransactionClick
            )
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
