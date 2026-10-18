package dev.muffar.moneyfikasi.domain.usecase.debt

data class DebtUseCases(
    val getAllDebts: GetAllDebts,
    val getDebtById: GetDebtById,
    val getDebtByTransactionId: GetDebtByTransactionId,
    val getDebtByHistoryTransactionId: GetDebtByHistoryTransactionId,
    val upsertDebt: UpsertDebt,
    val deleteDebt: DeleteDebt,
    val recordDebtPayment: RecordDebtPayment,
    val increaseDebtAmount: IncreaseDebtAmount,
    val updateDebtHistory: UpdateDebtHistory,
    val deleteDebtHistory: DeleteDebtHistory,
    val getDebtHistory: GetDebtHistory
)
