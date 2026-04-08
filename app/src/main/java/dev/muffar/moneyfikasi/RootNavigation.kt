package dev.muffar.moneyfikasi

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.muffar.moneyfikasi.backup_restore.navigation.backupRestoreNavGraph
import dev.muffar.moneyfikasi.backup_restore.navigation.toBackupRestoreScreen
import dev.muffar.moneyfikasi.category.add_edit.navigation.toAddEditCategoryScreen
import dev.muffar.moneyfikasi.category.categoriesNavGraph
import dev.muffar.moneyfikasi.category.list.navigation.toCategoriesScreen
import dev.muffar.moneyfikasi.feature.home.navigation.homeNavigation
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.preset.add_edit.navigation.toAddEditPresetScreen
import dev.muffar.moneyfikasi.preset.list.navigation.toPresetListScreen
import dev.muffar.moneyfikasi.preset.presetGraph
import dev.muffar.moneyfikasi.search.navigation.searchNavigation
import dev.muffar.moneyfikasi.search.navigation.toSearchScreen
import dev.muffar.moneyfikasi.settings.navigation.settingsNavGraph
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
fun RootNavigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
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
            onPresetClick = {},
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
            onNavigateToStatisticDetail = { dateRange, categoryId ->
                navController.toStatisticDetailScreen(
                    dateRange,
                    categoryId.toString()
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

        settingsNavGraph(
            navigateToWallets = { navController.toWalletsScreen() },
            navigateToCategories = { navController.toCategoriesScreen() },
            navigateToPreset = { navController.toPresetListScreen() },
            navigateToBackupRestore = { navController.toBackupRestoreScreen() }
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
            navigateBack = { navController.navigateUp() }
        )

        backupRestoreNavGraph(
            navigateBack = { navController.navigateUp() }
        )
    }
}