package dev.muffar.moneyfikasi.feature.dashboard

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
import dev.muffar.moneyfikasi.feature.dashboard.component.ReportCard
import dev.muffar.moneyfikasi.feature.dashboard.component.TotalBalance

@Composable
fun DashboardScreen(
    state: DashboardState,
    onToggleBalanceVisibility: () -> Unit,
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            ReportCard(
                income = state.reportIncome,
                expense = state.reportExpense,
                balance = state.reportBalance
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Other dashboard components
        }
    }
}