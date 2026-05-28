package dev.muffar.moneyfikasi.domain.usecase.recurring_transaction

data class RecurringTransactionUseCases(
    val getAllRecurringTransactions: GetAllRecurringTransactions,
    val getRecurringTransactionById: GetRecurringTransactionById,
    val saveRecurringTransaction: SaveRecurringTransaction,
    val deleteRecurringTransaction: DeleteRecurringTransaction,
    val processRecurringTransactions: ProcessRecurringTransactions,
    val getTransactionCountByRecurringId: GetTransactionCountByRecurringId,
    val checkActiveRecurringTransactions: CheckActiveRecurringTransactions,
)
