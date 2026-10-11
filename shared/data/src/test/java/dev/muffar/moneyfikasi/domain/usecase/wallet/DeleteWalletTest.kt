package dev.muffar.moneyfikasi.domain.usecase.wallet

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import dev.muffar.moneyfikasi.domain.repository.WalletRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.UUID

class DeleteWalletTest {

    private val walletRepository: WalletRepository = mockk(relaxed = true)
    private val debtRepository: DebtRepository = mockk(relaxed = true)
    private val transactionRepository: TransactionRepository = mockk(relaxed = true)

    private lateinit var deleteWallet: DeleteWallet

    private val walletId = UUID.randomUUID()
    private val wallet = Wallet(id = walletId, name = "Main")

    @Before
    fun setUp() {
        deleteWallet = DeleteWallet(
            walletRepository = walletRepository,
            debtRepository = debtRepository,
            transactionRepository = transactionRepository
        )
    }

    @Test
    fun `deletes linked transactions and debts before wallet`() = runTest {
        val transactionId = UUID.randomUUID()
        val debt = Debt(
            name = "Car loan",
            totalAmount = 1000.0,
            walletId = walletId,
            transactionId = transactionId
        )
        coEvery { debtRepository.getDebtsByWalletId(walletId) } returns listOf(debt)

        deleteWallet(wallet)

        coVerifyOrder {
            debtRepository.getDebtsByWalletId(walletId)
            transactionRepository.deleteTransaction(transactionId)
            debtRepository.deleteDebt(debt)
            walletRepository.deleteWallet(wallet)
        }
    }

    @Test
    fun `deletes debt without linked transaction`() = runTest {
        val debt = Debt(
            name = "Legacy debt",
            totalAmount = 500.0,
            walletId = walletId,
            transactionId = null
        )
        coEvery { debtRepository.getDebtsByWalletId(walletId) } returns listOf(debt)

        deleteWallet(wallet)

        coVerify(exactly = 0) { transactionRepository.deleteTransaction(any()) }
        coVerify { debtRepository.deleteDebt(debt) }
        coVerify { walletRepository.deleteWallet(wallet) }
    }

    @Test
    fun `only deletes wallet when no debts`() = runTest {
        coEvery { debtRepository.getDebtsByWalletId(walletId) } returns emptyList()

        deleteWallet(wallet)

        coVerify(exactly = 0) { transactionRepository.deleteTransaction(any()) }
        coVerify(exactly = 0) { debtRepository.deleteDebt(any()) }
        coVerify { walletRepository.deleteWallet(wallet) }
    }
}
