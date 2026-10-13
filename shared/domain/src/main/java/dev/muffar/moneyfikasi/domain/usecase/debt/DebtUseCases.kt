package dev.muffar.moneyfikasi.domain.usecase.debt

data class DebtUseCases(
    val getAllDebts: GetAllDebts,
    val getDebtById: GetDebtById,
    val getDebtByTransactionId: GetDebtByTransactionId,
    val upsertDebt: UpsertDebt,
    val deleteDebt: DeleteDebt,
    val recordDebtPayment: RecordDebtPayment,
    val increaseDebtAmount: IncreaseDebtAmount,
    val getDebtHistory: GetDebtHistory
)
