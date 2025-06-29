package dev.muffar.moneyfikasi

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.backup_restore.navigation.backupRestoreNavGraph
import dev.muffar.moneyfikasi.backup_restore.navigation.toBackupRestoreScreen
import dev.muffar.moneyfikasi.category.add_edit.navigation.toAddEditCategoryScreen
import dev.muffar.moneyfikasi.category.categoriesNavGraph
import dev.muffar.moneyfikasi.category.list.navigation.toCategoriesScreen
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.search.navigation.searchNavigation
import dev.muffar.moneyfikasi.settings.navigation.settingsNavGraph
import dev.muffar.moneyfikasi.statistic.detail.navigation.toStatisticDetailScreen
import dev.muffar.moneyfikasi.statistic.statisticNavGraph
import dev.muffar.moneyfikasi.transaction.add_edit.navigation.toAddEditTransactionScreen
import dev.muffar.moneyfikasi.transaction.detail.navigation.toTransactionDetail
import dev.muffar.moneyfikasi.transaction.transactionsNavGraph
import dev.muffar.moneyfikasi.wallet.add_edit.navigation.toAddEditWalletScreen
import dev.muffar.moneyfikasi.wallet.list.navigation.toWalletsScreen
import dev.muffar.moneyfikasi.wallet.walletsNavGraph

@Composable
fun RootNavigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Transactions.route
    ) {

        transactionsNavGraph(
            onNavigateBack = { navController.navigateUp() },
            onNavigateToTransactionDetail = { navController.toTransactionDetail(it) },
            onNavigateToEditTransaction = { type, id ->
                navController.toAddEditTransactionScreen(type, id)
            },
            onNavigateToAddTransaction = { type ->
                navController.toAddEditTransactionScreen(type)
            },
            onNavigateToAddWallet = { navController.toAddEditWalletScreen() },
            onNavigateToAddCategory = { navController.toAddEditCategoryScreen(it) }
        )

        statisticNavGraph(
            transactions = navController.previousBackStackEntry
                ?.savedStateHandle?.get<List<Transaction>>(Screen.StatisticDetail.TRANSACTIONS)
                ?: emptyList(),
            onNavigateToStatisticDetail = { navController.toStatisticDetailScreen(it) },
            onNavigateToTransactionDetail = { navController.toTransactionDetail(it) },
            onNavigateBack = { navController.navigateUp() }
        )

        searchNavigation(
            onNavigateToTransactionDetail = { navController.toTransactionDetail(it) },
        )

        composable(Screen.Debt.route) {
            Text("Debt")
        }

        settingsNavGraph(
            navigateToWallets = { navController.toWalletsScreen() },
            navigateToCategories = { navController.toCategoriesScreen() },
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

        backupRestoreNavGraph(
            navigateBack = { navController.navigateUp() }
        )
    }
}