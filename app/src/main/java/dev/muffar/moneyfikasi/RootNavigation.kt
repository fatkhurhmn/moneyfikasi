package dev.muffar.moneyfikasi

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.category.add_edit.navigation.toAddEditCategoryScreen
import dev.muffar.moneyfikasi.category.categoriesNavGraph
import dev.muffar.moneyfikasi.category.list.navigation.toCategoriesScreen
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.settings.navigation.settingsNavGraph
import dev.muffar.moneyfikasi.wallet.add_edit.navigation.addEditWalletNavigation
import dev.muffar.moneyfikasi.wallet.add_edit.navigation.toAddEditWalletScreen
import dev.muffar.moneyfikasi.wallet.list.navigation.toWalletsScreen
import dev.muffar.moneyfikasi.wallet.list.navigation.walletsNavigation

@Composable
fun RootNavigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Transaction.route
    ) {
        composable(Screen.Transaction.route) {
            Text("Transaction")
        }

        composable(Screen.Statistics.route) {
            Text("Statistics")
        }

        composable(Screen.Debt.route) {
            Text("Debt")
        }

        settingsNavGraph(
            navigateToWallets = { navController.toWalletsScreen() },
            navigateToCategories = { navController.toCategoriesScreen() }
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

        walletsNavigation (
            navigateToAddWallet = { navController.toAddEditWalletScreen() },
            navigateBack = { navController.navigateUp() }
        )

        addEditWalletNavigation {
            navController.navigateUp()
        }
    }
}