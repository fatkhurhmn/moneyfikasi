package dev.muffar.moneyfikasi.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Transaction : Screen("transaction")
    data object Statistics : Screen("statistics")
    data object Debt : Screen("debt")
    data object Settings : Screen("settings")
}