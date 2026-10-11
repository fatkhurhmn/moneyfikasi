package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.AppIcon
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.Colors
import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import dev.muffar.moneyfikasi.utils.constants.UUIDConst
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId

class UpsertDebt(
    private val debtRepository: DebtRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(debt: Debt) {
        ensureDebtCategory()
        val existing = debtRepository.getDebtById(debt.id)
        val existingTransactionId = existing?.transactionId ?: debt.transactionId
        val date = Instant.ofEpochMilli(debt.createdAt)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        if (existingTransactionId == null) {
            val transactionId = transactionRepository.addIncomeOrExpense(
                amount = debt.totalAmount,
                type = TransactionType.EXPENSE,
                date = date,
                note = debt.name,
                walletId = debt.walletId,
                categoryId = UUIDConst.DebtCategoryId
            )
            debtRepository.upsertDebt(debt.copy(transactionId = transactionId))
        } else {
            transactionRepository.updateIncomeOrExpense(
                id = existingTransactionId,
                amount = debt.totalAmount,
                type = TransactionType.EXPENSE,
                date = date,
                note = debt.name,
                walletId = debt.walletId,
                categoryId = UUIDConst.DebtCategoryId
            )
            debtRepository.upsertDebt(debt.copy(transactionId = existingTransactionId))
        }
    }

    private suspend fun ensureDebtCategory() {
        if (categoryRepository.getCategoryById(UUIDConst.DebtCategoryId) == null) {
            categoryRepository.upsertCategory(
                Category(
                    id = UUIDConst.DebtCategoryId,
                    name = "Debt",
                    icon = AppIcon.Receipt.name,
                    color = Colors.Red20.color,
                    type = CategoryType.EXPENSE,
                    isActive = false
                )
            )
        }
    }
}
