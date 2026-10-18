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
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDateTime
import java.util.UUID

class DeleteDebtTest {

    private val debtRepository: DebtRepository = mockk(relaxed = true)
    private val transactionRepository: TransactionRepository = mockk(relaxed = true)

    private lateinit var deleteDebt: DeleteDebt

    private val wallet = Wallet(id = UUID.randomUUID(), name = "Main")
    private val category = Category(id = UUID.randomUUID(), name = "Debt", type = CategoryType.INCOME)

    private fun transaction(id: UUID) = Transaction(
        id = id,
        category = category,
        wallet = wallet,
        type = TransactionType.EXPENSE,
        amount = 100.0,
        date = LocalDateTime.of(2026, 1, 2, 10, 0)
    )

    @Before
    fun setUp() {
        deleteDebt = DeleteDebt(
            debtRepository = debtRepository,
            transactionRepository = transactionRepository
        )
    }

    @Test
    fun `deletes principal and history before debt without duplicates`() = runTest {
        val debtId = UUID.randomUUID()
        val principalId = UUID.randomUUID()
        val paymentId = UUID.randomUUID()
        val debt = Debt(
            id = debtId,
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            walletId = wallet.id,
            transactionId = principalId
        )
        coEvery { debtRepository.getDebtById(debtId) } returns debt
        coEvery { transactionRepository.getTransactionsByDebtId(debtId) } returns
            flowOf(listOf(transaction(principalId), transaction(paymentId)))

        deleteDebt(debt)

        coVerifyOrder {
            debtRepository.getDebtById(debtId)
            transactionRepository.deleteTransaction(principalId)
            transactionRepository.deleteTransaction(paymentId)
            debtRepository.deleteDebt(debt)
        }
        coVerify(exactly = 1) { transactionRepository.deleteTransaction(principalId) }
    }

    @Test
    fun `deletes legacy principal missing from history`() = runTest {
        val debtId = UUID.randomUUID()
        val principalId = UUID.randomUUID()
        val paymentId = UUID.randomUUID()
        val debt = Debt(
            id = debtId,
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            walletId = wallet.id,
            transactionId = principalId
        )
        coEvery { debtRepository.getDebtById(debtId) } returns debt
        coEvery { transactionRepository.getTransactionsByDebtId(debtId) } returns
            flowOf(listOf(transaction(paymentId)))

        deleteDebt(debt)

        coVerifyOrder {
            transactionRepository.deleteTransaction(principalId)
            transactionRepository.deleteTransaction(paymentId)
            debtRepository.deleteDebt(debt)
        }
    }

    @Test
    fun `deletes only debt when no principal and no history`() = runTest {
        val debt = Debt(
            type = DebtType.DEBT,
            totalAmount = 500.0,
            walletId = wallet.id,
            transactionId = null
        )
        coEvery { debtRepository.getDebtById(debt.id) } returns debt
        coEvery { transactionRepository.getTransactionsByDebtId(debt.id) } returns
            flowOf(emptyList())

        deleteDebt(debt)

        coVerify(exactly = 0) { transactionRepository.deleteTransaction(any()) }
        coVerify { debtRepository.deleteDebt(debt) }
    }

    @Test
    fun `falls back to argument when stored debt is missing`() = runTest {
        val debtId = UUID.randomUUID()
        val principalId = UUID.randomUUID()
        val paymentId = UUID.randomUUID()
        val debt = Debt(
            id = debtId,
            type = DebtType.RECEIVABLE,
            totalAmount = 800.0,
            walletId = wallet.id,
            transactionId = principalId
        )
        coEvery { debtRepository.getDebtById(debtId) } returns null
        coEvery { transactionRepository.getTransactionsByDebtId(debtId) } returns
            flowOf(listOf(transaction(paymentId)))

        deleteDebt(debt)

        coVerify { transactionRepository.deleteTransaction(principalId) }
        coVerify { transactionRepository.deleteTransaction(paymentId) }
        coVerify { debtRepository.deleteDebt(debt) }
    }

    @Test
    fun `deletes history when principal is already gone`() = runTest {
        val debtId = UUID.randomUUID()
        val paymentId = UUID.randomUUID()
        val debt = Debt(
            id = debtId,
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            walletId = wallet.id,
            transactionId = null
        )
        coEvery { debtRepository.getDebtById(debtId) } returns debt
        coEvery { transactionRepository.getTransactionsByDebtId(debtId) } returns
            flowOf(listOf(transaction(paymentId)))

        deleteDebt(debt)

        coVerify { transactionRepository.deleteTransaction(paymentId) }
        coVerify { debtRepository.deleteDebt(debt) }
    }
}
