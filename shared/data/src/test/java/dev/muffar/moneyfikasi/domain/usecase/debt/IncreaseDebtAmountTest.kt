package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.TransactionType
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
import java.util.UUID

class IncreaseDebtAmountTest {

    private val debtRepository: DebtRepository = mockk(relaxed = true)
    private val transactionRepository: TransactionRepository = mockk(relaxed = true)

    private lateinit var increaseDebtAmount: IncreaseDebtAmount

    private val walletId = UUID.randomUUID()

    @Before
    fun setUp() {
        increaseDebtAmount = IncreaseDebtAmount(
            debtRepository = debtRepository,
            transactionRepository = transactionRepository
        )
        coEvery {
            transactionRepository.addIncomeOrExpense(any(), any(), any(), any(), any(), any(), any())
        } returns UUID.randomUUID()
    }

    @Test
    fun `DEBT increase writes INCOME and raises totalAmount`() = runTest {
        val debtId = UUID.randomUUID()
        val debt = Debt(
            id = debtId,
            name = "Borrowed money",
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            paidAmount = 200.0,
            walletId = walletId
        )
        coEvery { debtRepository.getDebtById(debtId) } returns debt

        increaseDebtAmount(debtId, 500.0)

        coVerify {
            transactionRepository.addIncomeOrExpense(
                amount = 500.0,
                type = TransactionType.INCOME,
                date = any(),
                note = "Borrowed money",
                walletId = walletId,
                categoryId = UUIDConst.DebtCategoryId,
                debtId = debtId
            )
        }
        coVerify {
            debtRepository.upsertDebt(
                match { it.totalAmount == 1500.0 && it.paidAmount == 200.0 }
            )
        }
    }

    @Test
    fun `RECEIVABLE increase writes EXPENSE and raises totalAmount`() = runTest {
        val debtId = UUID.randomUUID()
        val debt = Debt(
            id = debtId,
            name = "Lent money",
            type = DebtType.RECEIVABLE,
            totalAmount = 1000.0,
            walletId = walletId
        )
        coEvery { debtRepository.getDebtById(debtId) } returns debt

        increaseDebtAmount(debtId, 250.0)

        coVerify {
            transactionRepository.addIncomeOrExpense(
                amount = 250.0,
                type = TransactionType.EXPENSE,
                date = any(),
                note = "Lent money",
                walletId = walletId,
                categoryId = UUIDConst.ReceivableCategoryId,
                debtId = debtId
            )
        }
        coVerify { debtRepository.upsertDebt(match { it.totalAmount == 1250.0 }) }
    }

    @Test
    fun `non-positive amount is rejected`() = runTest {
        val debtId = UUID.randomUUID()

        var error: Throwable? = null
        try {
            increaseDebtAmount(debtId, 0.0)
        } catch (e: IllegalArgumentException) {
            error = e
        }

        assertEquals("Amount must be greater than 0", error?.message)
        coVerify(exactly = 0) { debtRepository.getDebtById(any()) }
    }

    @Test
    fun `missing debt is rejected`() = runTest {
        val debtId = UUID.randomUUID()
        coEvery { debtRepository.getDebtById(debtId) } returns null

        var error: Throwable? = null
        try {
            increaseDebtAmount(debtId, 100.0)
        } catch (e: IllegalArgumentException) {
            error = e
        }

        assertEquals("Debt not found", error?.message)
        coVerify(exactly = 0) { transactionRepository.addIncomeOrExpense(any(), any(), any(), any(), any(), any(), any()) }
    }
}
