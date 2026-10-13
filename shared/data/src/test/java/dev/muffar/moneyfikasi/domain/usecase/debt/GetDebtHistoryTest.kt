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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDateTime
import java.util.UUID

class GetDebtHistoryTest {

    private val debtRepository: DebtRepository = mockk(relaxed = true)
    private val transactionRepository: TransactionRepository = mockk(relaxed = true)

    private lateinit var getDebtHistory: GetDebtHistory

    private val wallet = Wallet(id = UUID.randomUUID(), name = "Main")
    private val category = Category(id = UUID.randomUUID(), name = "Debt", type = CategoryType.INCOME)

    private fun transaction(id: UUID, day: Int) = Transaction(
        id = id,
        category = category,
        wallet = wallet,
        type = TransactionType.INCOME,
        amount = 100.0,
        date = LocalDateTime.of(2026, 1, day, 10, 0)
    )

    @Before
    fun setUp() {
        getDebtHistory = GetDebtHistory(
            transactionRepository = transactionRepository,
            debtRepository = debtRepository
        )
    }

    @Test
    fun `legacy principal without debt_id is merged in date order`() = runTest {
        val debtId = UUID.randomUUID()
        val principalId = UUID.randomUUID()
        val paymentId = UUID.randomUUID()
        coEvery { debtRepository.getDebtById(debtId) } returns Debt(
            id = debtId,
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            walletId = wallet.id,
            transactionId = principalId
        )
        coEvery { transactionRepository.getTransactionsByDebtId(debtId) } returns
            flowOf(listOf(transaction(paymentId, 5)))
        coEvery { transactionRepository.getTransactionById(principalId) } returns
            transaction(principalId, 1)

        val history = getDebtHistory(debtId).first()

        assertEquals(listOf(paymentId, principalId), history.map { it.id })
    }

    @Test
    fun `principal already linked is not fetched twice`() = runTest {
        val debtId = UUID.randomUUID()
        val principalId = UUID.randomUUID()
        coEvery { debtRepository.getDebtById(debtId) } returns Debt(
            id = debtId,
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            walletId = wallet.id,
            transactionId = principalId
        )
        coEvery { transactionRepository.getTransactionsByDebtId(debtId) } returns
            flowOf(listOf(transaction(principalId, 1)))

        val history = getDebtHistory(debtId).first()

        assertEquals(listOf(principalId), history.map { it.id })
        coVerify(exactly = 0) { transactionRepository.getTransactionById(any()) }
    }
}
