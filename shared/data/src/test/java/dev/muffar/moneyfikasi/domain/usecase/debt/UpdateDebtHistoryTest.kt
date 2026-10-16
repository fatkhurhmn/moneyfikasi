package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import dev.muffar.moneyfikasi.utils.constants.UUIDConst
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDateTime
import java.util.UUID

class UpdateDebtHistoryTest {

    private val debtRepository: DebtRepository = mockk(relaxed = true)
    private val transactionRepository: TransactionRepository = mockk(relaxed = true)

    private lateinit var updateDebtHistory: UpdateDebtHistory

    private val wallet = Wallet(id = UUID.randomUUID(), name = "Main")
    private val category = Category(
        id = UUIDConst.DebtCategoryId,
        name = "Debt",
        type = CategoryType.INCOME
    )
    private val date = LocalDateTime.of(2026, 1, 5, 10, 0)

    private fun row(id: UUID, type: TransactionType, amount: Double) = Transaction(
        id = id,
        category = category,
        wallet = wallet,
        type = type,
        amount = amount,
        date = date
    )

    @Before
    fun setUp() {
        updateDebtHistory = UpdateDebtHistory(
            debtRepository = debtRepository,
            transactionRepository = transactionRepository
        )
    }

    @Test
    fun `payment row edit adjusts paid by delta`() = runTest {
        val debtId = UUID.randomUUID()
        val rowId = UUID.randomUUID()
        coEvery { debtRepository.getDebtById(debtId) } returns Debt(
            id = debtId,
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            paidAmount = 200.0,
            walletId = wallet.id
        )
        coEvery { transactionRepository.getTransactionById(rowId) } returns
            row(rowId, TransactionType.EXPENSE, 200.0)

        updateDebtHistory(debtId, rowId, 300.0, date, wallet.id, "Borrowed money")

        coVerify { debtRepository.upsertDebt(match { it.paidAmount == 300.0 }) }
        coVerify {
            transactionRepository.updateIncomeOrExpense(
                id = rowId,
                amount = 300.0,
                type = TransactionType.EXPENSE,
                date = date,
                note = "Borrowed money",
                walletId = wallet.id,
                categoryId = category.id,
                debtId = debtId
            )
        }
    }

    @Test
    fun `increase row edit adjusts total by delta`() = runTest {
        val debtId = UUID.randomUUID()
        val rowId = UUID.randomUUID()
        coEvery { debtRepository.getDebtById(debtId) } returns Debt(
            id = debtId,
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            paidAmount = 200.0,
            walletId = wallet.id
        )
        coEvery { transactionRepository.getTransactionById(rowId) } returns
            row(rowId, TransactionType.INCOME, 500.0)

        updateDebtHistory(debtId, rowId, 700.0, date, wallet.id, "Borrowed money")

        coVerify { debtRepository.upsertDebt(match { it.totalAmount == 1200.0 }) }
    }

    @Test
    fun `payment edit beyond total is allowed`() = runTest {
        val debtId = UUID.randomUUID()
        val rowId = UUID.randomUUID()
        coEvery { debtRepository.getDebtById(debtId) } returns Debt(
            id = debtId,
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            paidAmount = 900.0,
            walletId = wallet.id
        )
        coEvery { transactionRepository.getTransactionById(rowId) } returns
            row(rowId, TransactionType.EXPENSE, 100.0)

        updateDebtHistory(debtId, rowId, 300.0, date, wallet.id, null)

        coVerify { debtRepository.upsertDebt(match { it.paidAmount == 1100.0 }) }
    }

    @Test
    fun `increase edit below paid is rejected`() = runTest {
        val debtId = UUID.randomUUID()
        val rowId = UUID.randomUUID()
        coEvery { debtRepository.getDebtById(debtId) } returns Debt(
            id = debtId,
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            paidAmount = 900.0,
            walletId = wallet.id
        )
        coEvery { transactionRepository.getTransactionById(rowId) } returns
            row(rowId, TransactionType.INCOME, 500.0)

        var error: Throwable? = null
        try {
            updateDebtHistory(debtId, rowId, 100.0, date, wallet.id, null)
        } catch (e: IllegalArgumentException) {
            error = e
        }

        assertEquals("Total cannot be less than paid amount", error?.message)
        coVerify(exactly = 0) { debtRepository.upsertDebt(any()) }
    }
}
