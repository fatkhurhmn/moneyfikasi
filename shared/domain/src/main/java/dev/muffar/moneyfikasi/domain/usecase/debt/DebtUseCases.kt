package dev.muffar.moneyfikasi.domain.usecase.debt

data class DebtUseCases(
    val getAllDebts: GetAllDebts,
    val getDebtById: GetDebtById,
    val upsertDebt: UpsertDebt,
    val deleteDebt: DeleteDebt
)
