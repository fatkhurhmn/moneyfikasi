package dev.muffar.moneyfikasi.navigation

import dev.muffar.moneyfikasi.domain.model.CategoryType

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Transaction : Screen("transaction")
    data object Statistics : Screen("statistics")
    data object Debt : Screen("debt")
    data object Settings : Screen("settings")

    data object Categories : Screen("categories")
    data object AddCategory : Screen("add_category/{type}") {
        const val TYPE = "type"
        fun routeWithArg(type: CategoryType) = "add_category/$type"
    }
}