package dev.muffar.moneyfikasi.navigation

import dev.muffar.moneyfikasi.domain.model.CategoryType
import java.util.UUID

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Transaction : Screen("transaction")
    data object Statistics : Screen("statistics")
    data object Debt : Screen("debt")
    data object Settings : Screen("settings")

    data object Categories : Screen("categories")
    data object AddEditCategory : Screen("add_edit_category/{type}?category_id={category_id}") {
        const val TYPE = "type"
        const val CATEGORY_ID = "category_id"
        fun routeWithArg(type: CategoryType, id: UUID? = null): String {
            val categoryId = id?.toString() ?: ""
            return "add_edit_category/$type?$CATEGORY_ID=$categoryId"
        }
    }

    data object Wallets : Screen("wallets")
}