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

class GetDebtByTransactionIdTest {

    private val debtRepository: DebtRepository = mockk(relaxed = true)
    private lateinit var getDebtByTransactionId: GetDebtByTransactionId

    @Before
    fun setUp() {
        getDebtByTransactionId = GetDebtByTransactionId(debtRepository)
    }

    @Test
    fun `returns linked debt for transaction id`() = runTest {
        val transactionId = UUID.randomUUID()
        val debt = Debt(
            name = "Borrowed money",
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            transactionId = transactionId
        )
        coEvery { debtRepository.getDebtByTransactionId(transactionId) } returns debt

        val result = getDebtByTransactionId(transactionId)

        assertEquals(debt, result)
        coVerify { debtRepository.getDebtByTransactionId(transactionId) }
    }

    @Test
    fun `returns null when transaction has no linked debt`() = runTest {
        val transactionId = UUID.randomUUID()
        coEvery { debtRepository.getDebtByTransactionId(transactionId) } returns null

        val result = getDebtByTransactionId(transactionId)

        assertNull(result)
    }
}
