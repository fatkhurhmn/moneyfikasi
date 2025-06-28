package dev.muffar.moneyfikasi.domain.usecase.transaction

data class TransactionUseCases(
    val saveTransaction: SaveTransaction,
    val saveAllTransactions: SaveAllTransactions,
    val deleteTransaction: DeleteTransaction,
    val deleteAllTransactions: DeleteAllTransactions,
    val getAllTransactions: GetAllTransactions,
    val getTransactionById: GetTransactionById,
    val getTransactions: GetTransactions,
    val saveTransactionImage: SaveTransactionImage
)
