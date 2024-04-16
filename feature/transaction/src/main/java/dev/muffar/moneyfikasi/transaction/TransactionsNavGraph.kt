package dev.muffar.moneyfikasi.transaction

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.transaction.add_edit.navigation.addEditTransactionNavigation
import dev.muffar.moneyfikasi.transaction.detail.navigation.transactionDetailNavigation
import dev.muffar.moneyfikasi.transaction.list.navigation.transactionsNavigation
import java.util.UUID

fun NavGraphBuilder.transactionsNavGraph(
    onNavigateToTransactionDetail: (UUID) -> Unit,
    onNavigateToEditTransaction : (TransactionType, UUID) -> Unit,
    onNavigateToAddWallet : () -> Unit,
    onNavigateToAddCategory : (CategoryType) -> Unit,
    onNavigateBack: () -> Unit,
) {

    transactionsNavigation(
        onNavigateToTransactionDetail = onNavigateToTransactionDetail
    )

    addEditTransactionNavigation(
        onNavigateBack = onNavigateBack,
        onNavigateToAddWallet = onNavigateToAddWallet,
        onNavigateToAddCategory = onNavigateToAddCategory
    )

    transactionDetailNavigation(
        onNavigateToEditTransaction = onNavigateToEditTransaction,
        onNavigateBack = onNavigateBack,
    )
}