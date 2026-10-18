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

class RecordDebtPaymentTest {

    private val debtRepository: DebtRepository = mockk(relaxed = true)
    private val transactionRepository: TransactionRepository = mockk(relaxed = true)

    private lateinit var recordDebtPayment: RecordDebtPayment

    private val walletId = UUID.randomUUID()

    @Before
    fun setUp() {
        recordDebtPayment = RecordDebtPayment(
            debtRepository = debtRepository,
            transactionRepository = transactionRepository
        )
        coEvery {
            transactionRepository.addIncomeOrExpense(any(), any(), any(), any(), any(), any(), any())
        } returns UUID.randomUUID()
    }

    @Test
    fun `DEBT payment creates EXPENSE and increases paidAmount`() = runTest {
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

        recordDebtPayment(debtId, 300.0)

        coVerify {
            transactionRepository.addIncomeOrExpense(
                amount = 300.0,
                type = TransactionType.EXPENSE,
                date = any(),
                note = "Borrowed money",
                walletId = walletId,
                categoryId = UUIDConst.DebtCategoryId,
                debtId = debtId
            )
        }
        coVerify { debtRepository.upsertDebt(match { it.paidAmount == 500.0 }) }
    }

    @Test
    fun `RECEIVABLE collection creates INCOME and increases paidAmount`() = runTest {
        val debtId = UUID.randomUUID()
        val debt = Debt(
            id = debtId,
            name = "Lent money",
            type = DebtType.RECEIVABLE,
            totalAmount = 1000.0,
            paidAmount = 0.0,
            walletId = walletId
        )
        coEvery { debtRepository.getDebtById(debtId) } returns debt

        recordDebtPayment(debtId, 400.0)

        coVerify {
            transactionRepository.addIncomeOrExpense(
                amount = 400.0,
                type = TransactionType.INCOME,
                date = any(),
                note = "Lent money",
                walletId = walletId,
                categoryId = UUIDConst.ReceivableCategoryId,
                debtId = debtId
            )
        }
        coVerify { debtRepository.upsertDebt(match { it.paidAmount == 400.0 }) }
    }

    @Test
    fun `payment beyond total is allowed`() = runTest {
        val debtId = UUID.randomUUID()
        val debt = Debt(
            id = debtId,
            name = "Borrowed money",
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            paidAmount = 900.0,
            walletId = walletId
        )
        coEvery { debtRepository.getDebtById(debtId) } returns debt

        recordDebtPayment(debtId, 200.0)

        coVerify {
            transactionRepository.addIncomeOrExpense(
                amount = 200.0,
                type = TransactionType.EXPENSE,
                date = any(),
                note = "Borrowed money",
                walletId = walletId,
                categoryId = UUIDConst.DebtCategoryId,
                debtId = debtId
            )
        }
        coVerify { debtRepository.upsertDebt(match { it.paidAmount == 1100.0 }) }
    }

    @Test
    fun `non-positive amount is rejected`() = runTest {
        val debtId = UUID.randomUUID()

        var error: Throwable? = null
        try {
            recordDebtPayment(debtId, 0.0)
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
            recordDebtPayment(debtId, 100.0)
        } catch (e: IllegalArgumentException) {
            error = e
        }

        assertEquals("Debt not found", error?.message)
        coVerify(exactly = 0) { transactionRepository.addIncomeOrExpense(any(), any(), any(), any(), any(), any(), any()) }
    }
}
