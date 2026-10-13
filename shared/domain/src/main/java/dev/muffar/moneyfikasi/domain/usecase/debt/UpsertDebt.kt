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
import java.util.UUID

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
            UUIDConst.DebtCategoryId
        } else {
            UUIDConst.ReceivableCategoryId
        }

        if (existingTransactionId == null) {
            val transactionId = transactionRepository.addIncomeOrExpense(
                amount = debt.totalAmount,
                type = transactionType,
                date = date,
                note = debt.name,
                walletId = debt.walletId,
                categoryId = categoryId,
                debtId = debt.id
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
                categoryId = categoryId,
                debtId = debt.id
            )
            debtRepository.upsertDebt(debt.copy(transactionId = existingTransactionId))
        }
    }

    // Fallback names stay in English: domain has no Context access, and fresh installs
    // are prepopulated with localized names via InitDataSource. Rows created here with
    // English names are renamed on the next language change by UpdateDefaultCategories.
    private suspend fun ensureDebtCategories() {
        ensureDebtCategory(
            id = UUIDConst.DebtCategoryId,
            fallbackName = "Debt",
            type = CategoryType.INCOME,
            color = Colors.Green10.color
        )
        ensureDebtCategory(
            id = UUIDConst.ReceivableCategoryId,
            fallbackName = "Receivable",
            type = CategoryType.EXPENSE,
            color = Colors.Red20.color
        )
    }

    private suspend fun ensureDebtCategory(
        id: UUID,
        fallbackName: String,
        type: CategoryType,
        color: Long
    ) {
        val existing = categoryRepository.getCategoryById(id)
        if (existing == null) {
            categoryRepository.upsertCategory(
                Category(
                    id = id,
                    name = fallbackName,
                    icon = AppIcon.Receipt.name,
                    color = color,
                    type = type,
                    isActive = false
                )
            )
        } else if (existing.type != type || existing.color != color || existing.icon != AppIcon.Receipt.name) {
            // Heal rows written with the old swapped type/color: keep the user-visible
            // name untouched so manual renames and rename-on-locale keep working.
            categoryRepository.upsertCategory(
                existing.copy(
                    icon = AppIcon.Receipt.name,
                    color = color,
                    type = type
                )
            )
        }
    }
}
