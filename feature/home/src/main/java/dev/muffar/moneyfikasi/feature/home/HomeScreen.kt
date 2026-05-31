package dev.muffar.moneyfikasi.feature.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.ChooseDateSheet
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.CustomDateSheet
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.feature.home.component.BudgetSection
import dev.muffar.moneyfikasi.feature.home.component.DashboardSettingsSheet
import dev.muffar.moneyfikasi.feature.home.component.MoneyfikasiLogo
import dev.muffar.moneyfikasi.feature.home.component.QuickTransactionSection
import dev.muffar.moneyfikasi.feature.home.component.RecentTransactionsSection
import dev.muffar.moneyfikasi.feature.home.component.ReportSection
import dev.muffar.moneyfikasi.feature.home.component.WalletSection
import java.util.UUID

@Composable
fun HomeScreen(
    state: HomeState,
    onToggleBalanceVisibility: () -> Unit,
    onToggleReportVisibility: () -> Unit,
    onShowReportDateSheet: (Boolean) -> Unit,
    onShowCustomDateSheet: (Boolean) -> Unit,
    onShowDashboardSettingsSheet: (Boolean) -> Unit,
    onQuickTransactionVisibilityChange: (Boolean) -> Unit,
    onBudgetVisibilityChange: (Boolean) -> Unit,
    onDateRangeChange: (DateRange) -> Unit,
    onSeeAllTransactionsClick: () -> Unit,
    onTransactionClick: (UUID, Boolean) -> Unit,
    onPresetClick: (TransactionType, UUID) -> Unit,
    onAddPresetClick: () -> Unit,
    onPresetsClick: () -> Unit,
    onSeeAllBudgetsClick: () -> Unit,
    onAddBudgetClick: () -> Unit,
    onAddWalletClick: () -> Unit
) {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    Scaffold { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MoneyfikasiLogo(
                onDashboardSettingsClick = { onShowDashboardSettingsSheet(true) }
            )

            ReportSection(
                state = state,
                onDateRangeClick = { onShowReportDateSheet(true) },
                onVisibilityClick = onToggleReportVisibility,
            )

            WalletSection(
                wallets = state.wallets.toList(),
                totalBalance = state.totalBalance,
                isBalanceVisible = state.isBalanceVisible,
                onVisibilityClick = onToggleBalanceVisibility,
                onAddWalletClick = onAddWalletClick
            )

            if (state.isQuickTransactionVisible) {
                QuickTransactionSection(
                    presets = state.presets,
                    onPresetClick = onPresetClick,
                    onAddPresetClick = onAddPresetClick,
                    onPresetsClick = onPresetsClick
                )
            }

            if (state.isBudgetVisible) {
                BudgetSection(
                    budgets = state.budgets,
                    onBudgetClick = {},
                    onSeeAllBudgetsClick = onSeeAllBudgetsClick,
                    onAddBudgetClick = onAddBudgetClick
                )
            }

            RecentTransactionsSection(
                transactions = state.recentTransactions,
                onSeeAllTransactionsClick = onSeeAllTransactionsClick,
                onTransactionClick = onTransactionClick
            )
        }
    }

    AnimatedVisibility(state.showReportDateSheet) {
        ChooseDateSheet(
            dateRange = state.dateRange,
            periods = listOf(TimePeriod.DAILY, TimePeriod.WEEKLY, TimePeriod.MONTHLY),
            onDismissRequest = { onShowReportDateSheet(false) },
            onChoose = onDateRangeChange,
            onCustomDateClick = {
                onShowReportDateSheet(false)
                onShowCustomDateSheet(true)
            }
        )
    }

    AnimatedVisibility(state.showCustomDateSheet) {
        CustomDateSheet(
            dateRange = state.dateRange,
            onDateChange = onDateRangeChange,
            onDismissRequest = { onShowCustomDateSheet(false) }
        )
    }

    AnimatedVisibility(state.showDashboardSettingsSheet) {
        DashboardSettingsSheet(
            isQuickTransactionVisible = state.isQuickTransactionVisible,
            isBudgetVisible = state.isBudgetVisible,
            onQuickTransactionVisibilityChange = onQuickTransactionVisibilityChange,
            onBudgetVisibilityChange = onBudgetVisibilityChange,
            onDismissRequest = { onShowDashboardSettingsSheet(false) }
        )
    }
}
