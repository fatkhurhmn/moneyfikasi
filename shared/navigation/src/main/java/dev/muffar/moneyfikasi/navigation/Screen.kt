package dev.muffar.moneyfikasi.navigation

import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import java.util.UUID

sealed class Screen(val route: String) {
    data object Main : Screen("main")

    data object Debt : Screen("debt")

    data object Search : Screen("search")
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
    data object AddEditWallet : Screen("add_edit_wallet?wallet_id={wallet_id}") {
        const val TYPE = "type"
        const val WALLET_ID = "wallet_id"
        fun routeWithArg(id: UUID? = null): String {
            val walletId = id?.toString() ?: ""
            return "add_edit_wallet?$WALLET_ID=$walletId"
        }
    }

    data object Transactions : Screen("transactions")
    data object AddEditTransaction :
        Screen("add_edit_transaction/{type}?transaction_id={transaction_id}") {
        const val TYPE = "type"
        const val TRANSACTION_ID = "transaction_id"
        fun routeWithArg(type: TransactionType, id: UUID? = null): String {
            val transactionId = id?.toString() ?: ""
            return "add_edit_transaction/$type?$TRANSACTION_ID=$transactionId"
        }
    }

    data object TransactionDetail : Screen("transaction_detail/{transaction_id}") {
        const val TRANSACTION_ID = "transaction_id"
        fun routeWithArg(id: UUID): String {
            return "transaction_detail/$id"
        }
    }

    data object Statistic : Screen("statistic")
    data object StatisticDetail : Screen("statistic_detail") {
        const val TRANSACTIONS = "transactions"
    }
}