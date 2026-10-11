package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.AppIcon
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.Colors
import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.DebtType
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
        ensureDebtCategories()
        val existing = debtRepository.getDebtById(debt.id)
        val existingTransactionId = existing?.transactionId ?: debt.transactionId
        val date = Instant.ofEpochMilli(debt.createdAt)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        val transactionType = debt.type.toTransactionType()
        val categoryId = if (debt.type == DebtType.DEBT) {
            UUIDConst.ReceivableCategoryId
        } else {
            UUIDConst.DebtCategoryId
        }

        if (existingTransactionId == null) {
            val transactionId = transactionRepository.addIncomeOrExpense(
                amount = debt.totalAmount,
                type = transactionType,
                date = date,
                note = debt.name,
                walletId = debt.walletId,
                categoryId = categoryId
            )
            debtRepository.upsertDebt(debt.copy(transactionId = transactionId))
        } else {
            transactionRepository.updateIncomeOrExpense(
                id = existingTransactionId,
                amount = debt.totalAmount,
                type = transactionType,
                date = date,
                note = debt.name,
                walletId = debt.walletId,
                categoryId = categoryId
            )
            debtRepository.upsertDebt(debt.copy(transactionId = existingTransactionId))
        }
    }

    // Fallback names stay in English: domain has no Context access, and fresh installs
    // are prepopulated with localized names via InitDataSource. Rows created here with
    // English names are renamed on the next language change by UpdateDefaultCategories.
    private suspend fun ensureDebtCategories() {
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
        if (categoryRepository.getCategoryById(UUIDConst.ReceivableCategoryId) == null) {
            categoryRepository.upsertCategory(
                Category(
                    id = UUIDConst.ReceivableCategoryId,
                    name = "Receivable",
                    icon = AppIcon.Receipt.name,
                    color = Colors.Green10.color,
                    type = CategoryType.INCOME,
                    isActive = false
                )
            )
        }
    }
}
