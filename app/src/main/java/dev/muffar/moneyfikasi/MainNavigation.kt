package dev.muffar.moneyfikasi

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import dev.muffar.moneyfikasi.backup_restore.navigation.backupRestoreNavGraph
import dev.muffar.moneyfikasi.backup_restore.navigation.toBackupRestoreScreen
import dev.muffar.moneyfikasi.budget.add_edit.navigation.toAddEditBudgetScreen
import dev.muffar.moneyfikasi.budget.budgetsNavGraph
import dev.muffar.moneyfikasi.budget.list.navigation.toBudgetsScreen
import dev.muffar.moneyfikasi.category.add_edit.navigation.toAddEditCategoryScreen
import dev.muffar.moneyfikasi.category.categoriesNavGraph
import dev.muffar.moneyfikasi.category.list.navigation.toCategoriesScreen
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.export.navigation.exportNavGraph
import dev.muffar.moneyfikasi.export.navigation.toExportScreen
import dev.muffar.moneyfikasi.feature.applock.appLockNavGraph
import dev.muffar.moneyfikasi.feature.applock.enter_pin.navigation.toEnterPinScreen
import dev.muffar.moneyfikasi.feature.applock.main.navigation.toAppLockScreen
import dev.muffar.moneyfikasi.feature.home.navigation.homeNavigation
import dev.muffar.moneyfikasi.more.navigation.moreNavGraph
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.preset.add_edit.navigation.toAddEditPresetScreen
import dev.muffar.moneyfikasi.preset.list.navigation.toPresetsScreen
import dev.muffar.moneyfikasi.preset.presetGraph
import dev.muffar.moneyfikasi.recurring_transaction.navigation.recurringTransactionNavGraph
import dev.muffar.moneyfikasi.recurring_transaction.navigation.toRecurringTransactionsScreen
import dev.muffar.moneyfikasi.search.navigation.searchNavigation
import dev.muffar.moneyfikasi.search.navigation.toSearchScreen
import dev.muffar.moneyfikasi.settings.navigation.navigateToSettings
import dev.muffar.moneyfikasi.settings.navigation.settingsNavGraph
import dev.muffar.moneyfikasi.statistic.category_distribution.navigation.toCategoryDistributionScreen
import dev.muffar.moneyfikasi.statistic.detail.navigation.toStatisticDetailScreen
import dev.muffar.moneyfikasi.statistic.statisticNavGraph
import dev.muffar.moneyfikasi.transaction.add_edit.navigation.toAddEditTransactionScreen
import dev.muffar.moneyfikasi.transaction.detail.navigation.toTransactionDetail
import dev.muffar.moneyfikasi.transaction.transactionsNavGraph
import dev.muffar.moneyfikasi.transaction.transfer.navigation.toTransferTransactionScreen
import dev.muffar.moneyfikasi.wallet.add_edit.navigation.toAddEditWalletScreen
import dev.muffar.moneyfikasi.wallet.list.navigation.toWalletsScreen
import dev.muffar.moneyfikasi.wallet.walletsNavGraph

@Composable
fun MainNavigation(
    navController: NavHostController,
    startDestination: String,
) {
    AppNavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        homeNavigation(
            onTransactionClick = { id, isTransfer ->
                navController.toTransactionDetail(id, isTransfer)
            },
            onSeeAllTransactionsClick = {
                navController.navigate(Screen.Transactions.route) {
                    popUpTo(Screen.Home.route) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onPresetClick = { type, id ->
                navController.toAddEditTransactionScreen(type = type, presetId = id)
            },
            onSeeAllBudgetsClick = { navController.toBudgetsScreen() },
            navigateToAddPreset = { navController.toAddEditPresetScreen(TransactionType.INCOME) },
            navigateToPresets = { navController.toPresetsScreen() },
            navigateToAddBudget = { navController.toAddEditBudgetScreen() }
        )

        transactionsNavGraph(
            onNavigateBack = { navController.navigateUp() },
            onNavigateToTransactionDetail = { id, isTransfer ->
                navController.toTransactionDetail(id, isTransfer)
            },
            onNavigateToEditTransaction = { type, id ->
                if (type != null) {
                    navController.toAddEditTransactionScreen(type, id)
                } else {
                    navController.toTransferTransactionScreen(id)
                }
            },
            onNavigateToAddWallet = { navController.toAddEditWalletScreen() },
            onNavigateToAddCategory = { navController.toAddEditCategoryScreen(it) },
            onNavigateToSearch = { navController.toSearchScreen() },
        )

        statisticNavGraph(
            onNavigateToAllCategoryStatistic = { startDate, endDate ->
                navController.toCategoryDistributionScreen(startDate, endDate)
            },
            onNavigateToStatisticDetail = { dateRange, categoryId, categoryName ->
                navController.toStatisticDetailScreen(
                    dateRange,
                    categoryId.toString(),
                    categoryName
                )
            },
            onNavigateToTransactionDetail = { id, isTransfer ->
                navController.toTransactionDetail(id, isTransfer)
            },
            onNavigateBack = { navController.navigateUp() }
        )

        searchNavigation(
            onNavigateToTransactionDetail = { id, isTransfer ->
                navController.toTransactionDetail(id, isTransfer)
            },
            onNavigateBack = { navController.navigateUp() }
        )

        moreNavGraph(
            navigateToWallets = { navController.toWalletsScreen() },
            navigateToCategories = { navController.toCategoriesScreen() },
            navigateToPreset = { navController.toPresetsScreen() },
            navigateToBudgets = { navController.toBudgetsScreen() },
            navigateToRecurringTransactions = { navController.toRecurringTransactionsScreen() },
            navigateToSettings = { navController.navigateToSettings() },
        )

        settingsNavGraph(
            onBackupRestoreClick = { navController.toBackupRestoreScreen() },
            onExportClick = { navController.toExportScreen() },
            onAppLockClick = { navController.toAppLockScreen() },
            onBackClick = { navController.navigateUp() }
        )

        recurringTransactionNavGraph(
            navigateBack = { navController.navigateUp() }
        )

        categoriesNavGraph(
            navigateToAddCategory = {
                navController.toAddEditCategoryScreen(it)
            },
            navigateToEditCategory = { type, id ->
                navController.toAddEditCategoryScreen(type, id)
            },
            navigateBack = { navController.navigateUp() }
        )

        walletsNavGraph(
            navigateToAddWallet = { navController.toAddEditWalletScreen() },
            navigateToEditWallet = { navController.toAddEditWalletScreen(it) },
            navigateBack = { navController.navigateUp() }
        )

        presetGraph(
            navigateToAddPreset = { navController.toAddEditPresetScreen(it) },
            navigateToEditPreset = { type, id ->
                navController.toAddEditPresetScreen(type, id)
            },
            navigateToAddWallet = { navController.toAddEditWalletScreen() },
            navigateToAddCategory = { navController.toAddEditCategoryScreen(it) },
            navigateBack = { navController.navigateUp() }
        )

        budgetsNavGraph(
            navigateToAddBudget = { navController.toAddEditBudgetScreen() },
            navigateToEditBudget = { navController.toAddEditBudgetScreen(it) },
            navigateToAddCategory = { navController.toAddEditCategoryScreen(CategoryType.EXPENSE) },
            navigateBack = { navController.navigateUp() }
        )

        backupRestoreNavGraph(
            navigateBack = { navController.navigateUp() }
        )

        exportNavGraph(
            navigateBack = { navController.navigateUp() }
        )

        appLockNavGraph(
            navigateBack = { navController.navigateUp() },
            onNavigateToEnterPin = { navController.toEnterPinScreen(it) },
            onEnterPinSuccess = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        )
    }
}
