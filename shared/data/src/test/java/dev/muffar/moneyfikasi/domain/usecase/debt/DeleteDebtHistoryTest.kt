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
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDateTime
import java.util.UUID

class DeleteDebtHistoryTest {

    private val debtRepository: DebtRepository = mockk(relaxed = true)
    private val transactionRepository: TransactionRepository = mockk(relaxed = true)

    private lateinit var deleteDebtHistory: DeleteDebtHistory

    private val wallet = Wallet(id = UUID.randomUUID(), name = "Main")
    private val category = Category(id = UUID.randomUUID(), name = "Debt", type = CategoryType.INCOME)
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
        deleteDebtHistory = DeleteDebtHistory(
            debtRepository = debtRepository,
            transactionRepository = transactionRepository
        )
    }

    @Test
    fun `payment row delete reverses paid`() = runTest {
        val debtId = UUID.randomUUID()
        val rowId = UUID.randomUUID()
        coEvery { debtRepository.getDebtById(debtId) } returns Debt(
            id = debtId,
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            paidAmount = 300.0,
            walletId = wallet.id
        )
        coEvery { transactionRepository.getTransactionById(rowId) } returns
            row(rowId, TransactionType.EXPENSE, 300.0)

        deleteDebtHistory(debtId, rowId)

        coVerify { debtRepository.upsertDebt(match { it.paidAmount == 0.0 }) }
        coVerify { transactionRepository.deleteTransaction(rowId) }
    }

    @Test
    fun `increase row delete reverses total`() = runTest {
        val debtId = UUID.randomUUID()
        val rowId = UUID.randomUUID()
        coEvery { debtRepository.getDebtById(debtId) } returns Debt(
            id = debtId,
            type = DebtType.DEBT,
            totalAmount = 1200.0,
            paidAmount = 200.0,
            walletId = wallet.id
        )
        coEvery { transactionRepository.getTransactionById(rowId) } returns
            row(rowId, TransactionType.INCOME, 200.0)

        deleteDebtHistory(debtId, rowId)

        coVerify { debtRepository.upsertDebt(match { it.totalAmount == 1000.0 }) }
        coVerify { transactionRepository.deleteTransaction(rowId) }
    }

    @Test
    fun `increase row delete below paid is rejected`() = runTest {
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
            deleteDebtHistory(debtId, rowId)
        } catch (e: IllegalArgumentException) {
            error = e
        }

        assertEquals("Total cannot be less than paid amount", error?.message)
        coVerify(exactly = 0) { transactionRepository.deleteTransaction(any()) }
    }
}
