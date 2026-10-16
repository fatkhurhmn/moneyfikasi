package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.UUID

class GetDebtByHistoryTransactionIdTest {

    private val debtRepository: DebtRepository = mockk(relaxed = true)
    private lateinit var getDebtByHistoryTransactionId: GetDebtByHistoryTransactionId

    @Before
    fun setUp() {
        getDebtByHistoryTransactionId = GetDebtByHistoryTransactionId(debtRepository)
    }

    @Test
    fun `returns parent debt for history transaction id`() = runTest {
        val historyId = UUID.randomUUID()
        val debt = Debt(
            name = "Borrowed money",
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            transactionId = UUID.randomUUID()
        )
        coEvery { debtRepository.getDebtByHistoryTransactionId(historyId) } returns debt

        val result = getDebtByHistoryTransactionId(historyId)

        assertEquals(debt, result)
        coVerify { debtRepository.getDebtByHistoryTransactionId(historyId) }
    }

    @Test
    fun `returns null when transaction is not debt history`() = runTest {
        val transactionId = UUID.randomUUID()
        coEvery { debtRepository.getDebtByHistoryTransactionId(transactionId) } returns null

        val result = getDebtByHistoryTransactionId(transactionId)

        assertNull(result)
    }
}
