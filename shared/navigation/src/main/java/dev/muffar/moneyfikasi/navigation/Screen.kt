package dev.muffar.moneyfikasi.navigation

import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import java.util.UUID

sealed class Screen(val route: String) {
    data object Home : Screen("home")

    data object Search : Screen("search")
    data object More : Screen("more")
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
        const val WALLET_ID = "wallet_id"
        fun routeWithArg(id: UUID? = null): String {
            val walletId = id?.toString() ?: ""
            return "add_edit_wallet?$WALLET_ID=$walletId"
        }
    }

    data object Transactions : Screen("transactions")
    data object AddEditTransaction :
        Screen("add_edit_transaction/{type}?transaction_id={transaction_id}&preset_id={preset_id}") {
        const val TYPE = "type"
        const val TRANSACTION_ID = "transaction_id"
        const val PRESET_ID = "preset_id"
        fun routeWithArg(type: TransactionType, id: UUID? = null, presetId: UUID? = null): String {
            val transactionId = id?.toString() ?: ""
            val pId = presetId?.toString() ?: ""
            return "add_edit_transaction/$type?$TRANSACTION_ID=$transactionId&$PRESET_ID=$pId"
        }
    }

    data object TransferTransaction :
        Screen("transfer_transaction?transaction_id={transaction_id}") {
        const val TRANSACTION_ID = "transaction_id"
        fun routeWithArg(id: UUID? = null): String {
            val transactionId = id?.toString() ?: ""
            return "transfer_transaction?$TRANSACTION_ID=$transactionId"
        }
    }

    data object TransactionDetail : Screen("transaction_detail/{transaction_id}/{is_transfer}") {
        const val TRANSACTION_ID = "transaction_id"
        const val IS_TRANSFER = "is_transfer"
        fun routeWithArg(id: UUID, isTransfer: Boolean): String {
            return "transaction_detail/$id/$isTransfer"
        }
    }

    data object Statistic : Screen("statistic")
    data object CategoryDistribution :
        Screen("all_category_statistic?start_date={start_date}&end_date={end_date}") {
        const val START_DATE = "start_date"
        const val END_DATE = "end_date"
        fun routeWithArg(startDate: Long, endDate: Long): String {
            return "all_category_statistic?$START_DATE=$startDate&$END_DATE=$endDate"
        }
    }

    data object StatisticDetail :
        Screen("statistic_detail?start_date={start_date}&end_date={end_date}&category_id={category_id}&category_name={category_name}") {
        const val START_DATE = "start_date"
        const val END_DATE = "end_date"
        const val CATEGORY_ID = "category_id"
        const val CATEGORY_NAME = "category_name"
        fun routeWithArg(
            startDate: Long,
            endDate: Long,
            categoryId: String,
            categoryName: String
        ): String {
            return "statistic_detail?$START_DATE=$startDate&$END_DATE=$endDate&$CATEGORY_ID=$categoryId&$CATEGORY_NAME=$categoryName"
        }
    }

    data object BackupRestore : Screen("backup_restore")

    data object Presets : Screen("presets")
    data object AddEditPreset : Screen("add_edit_preset/{type}?preset_id={preset_id}") {
        const val TYPE = "type"
        const val PRESET_ID = "preset_id"
        fun routeWithArg(type: TransactionType, id: UUID? = null): String {
            val presetId = id?.toString() ?: ""
            return "add_edit_preset/$type?$PRESET_ID=$presetId"
        }
    }

    data object Budgets : Screen("budgets")
    data object AddEditBudget : Screen("add_edit_budget?budget_id={budget_id}") {
        const val BUDGET_ID = "budget_id"
        fun routeWithArg(id: UUID? = null): String {
            val budgetId = id?.toString() ?: ""
            return "add_edit_budget?$BUDGET_ID=$budgetId"
        }
    }

    data object RecurringTransactions : Screen("recurring_transactions")
    data object AddEditRecurringTransaction :
        Screen("add_edit_recurring_transaction/{type}?recurring_transaction_id={recurring_transaction_id}") {
        const val TYPE = "type"
        const val RECURRING_TRANSACTION_ID = "recurring_transaction_id"
        fun routeWithArg(type: TransactionType, id: UUID? = null): String {
            val recurringTransactionId = id?.toString() ?: ""
            return "add_edit_recurring_transaction/$type?$RECURRING_TRANSACTION_ID=$recurringTransactionId"
        }
    }

    data object Export : Screen("export")

    data object Notifications : Screen("notifications")

    data object AppLock : Screen("app_lock")

    data object EnterPin : Screen("enter_pin/{type}") {
        const val TYPE = "type"
        fun routeWithArg(type: dev.muffar.moneyfikasi.domain.model.EnterPinType): String {
            return "enter_pin/$type"
        }
    }
}
