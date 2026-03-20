package dev.muffar.moneyfikasi.transaction

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.transaction.add_edit.navigation.addEditTransactionNavigation
import dev.muffar.moneyfikasi.transaction.detail.navigation.transactionDetailNavigation
import dev.muffar.moneyfikasi.transaction.list.navigation.transactionsNavigation
import dev.muffar.moneyfikasi.transaction.transfer.navigation.transferTransactionNavigation
import java.util.UUID

fun NavGraphBuilder.transactionsNavGraph(
    onNavigateToTransactionDetail: (UUID, Boolean) -> Unit,
    onNavigateToEditTransaction: (TransactionType?, UUID) -> Unit,
    onNavigateToAddTransaction: (TransactionType) -> Unit,
    onNavigateToTransferTransaction: () -> Unit,
    onNavigateToAddWallet: () -> Unit,
    onNavigateToAddCategory: (CategoryType) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateBack: () -> Unit,
) {

    transactionsNavigation(
        onNavigateToTransactionDetail = onNavigateToTransactionDetail,
        onNavigateToAddScreen = onNavigateToAddTransaction,
        onNavigateToTransferScreen = onNavigateToTransferTransaction,
        onNavigateToSearch = onNavigateToSearch
    )

    addEditTransactionNavigation(
        onNavigateBack = onNavigateBack,
        onNavigateToAddWallet = onNavigateToAddWallet,
        onNavigateToAddCategory = onNavigateToAddCategory
    )

    transferTransactionNavigation(
        onNavigateBack = onNavigateBack,
        onNavigateToAddWallet = onNavigateToAddWallet
    )

    transactionDetailNavigation(
        onNavigateToEditTransaction = onNavigateToEditTransaction,
        onNavigateBack = onNavigateBack,
    )
}