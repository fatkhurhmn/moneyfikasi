package dev.muffar.moneyfikasi.domain.usecase.transaction

data class TransactionUseCases(
    val addTransaction: AddTransaction,
    val updateTransaction: UpdateTransaction,
    val deleteTransaction: DeleteTransaction,
    val getAllTransactions: GetAllTransactions,
    val getTransactionById: GetTransactionById,
    val getTransactions: GetTransactions,
    val addTransfer: AddTransfer,
    val updateTransfer: UpdateTransfer,
    val getTransferDetail: GetTransferDetail,
    val saveTransactionImage: SaveTransactionImage
)
