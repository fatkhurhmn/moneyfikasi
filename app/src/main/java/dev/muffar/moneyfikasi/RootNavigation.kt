package dev.muffar.moneyfikasi

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.category.list.navigation.categoriesNavGraph
import dev.muffar.moneyfikasi.category.list.navigation.toCategoriesScreen
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.settings.navigation.settingsNavGraph

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
            navigateToCategories = { navController.toCategoriesScreen() }
        )

        categoriesNavGraph()
    }
}