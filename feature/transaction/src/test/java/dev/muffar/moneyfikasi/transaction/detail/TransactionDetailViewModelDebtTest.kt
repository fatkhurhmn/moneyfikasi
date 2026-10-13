package dev.muffar.moneyfikasi.transaction.detail

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.TransferDetail
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import dev.muffar.moneyfikasi.domain.usecase.debt.DebtUseCases
import dev.muffar.moneyfikasi.domain.usecase.debt.DeleteDebt
import dev.muffar.moneyfikasi.domain.usecase.debt.GetAllDebts
import dev.muffar.moneyfikasi.domain.usecase.debt.GetDebtById
import dev.muffar.moneyfikasi.domain.usecase.debt.GetDebtByTransactionId
import dev.muffar.moneyfikasi.domain.usecase.debt.UpsertDebt
import dev.muffar.moneyfikasi.domain.usecase.transaction.AddTransaction
import dev.muffar.moneyfikasi.domain.usecase.transaction.AddTransfer
import dev.muffar.moneyfikasi.domain.usecase.transaction.DeleteTransaction
import dev.muffar.moneyfikasi.domain.usecase.transaction.GetAllTransactions
import dev.muffar.moneyfikasi.domain.usecase.transaction.GetAllTransactionsPaged
import dev.muffar.moneyfikasi.domain.usecase.transaction.GetExpenseSum
import dev.muffar.moneyfikasi.domain.usecase.transaction.GetIncomeSum
import dev.muffar.moneyfikasi.domain.usecase.transaction.GetNetBalance
import dev.muffar.moneyfikasi.domain.usecase.transaction.GetRecentTransactions
import dev.muffar.moneyfikasi.domain.usecase.transaction.GetTransactionById
import dev.muffar.moneyfikasi.domain.usecase.transaction.GetTransactions
import dev.muffar.moneyfikasi.domain.usecase.transaction.GetTransactionsPaged
import dev.muffar.moneyfikasi.domain.usecase.transaction.GetTransferDetail
import dev.muffar.moneyfikasi.domain.usecase.transaction.SaveTransactionImage
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.UpdateTransaction
import dev.muffar.moneyfikasi.domain.usecase.transaction.UpdateTransfer
import dev.muffar.moneyfikasi.navigation.Screen
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionDetailViewModelDebtTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val testCategory = Category(
        id = UUID.randomUUID(),
        name = "Receivable",
        type = CategoryType.INCOME,
        isActive = false
    )
    private val testWallet = Wallet(id = UUID.randomUUID(), name = "Main", balance = 1000.0)

    private lateinit var fakeTransactionRepo: FakeTransactionRepository
    private lateinit var fakeDebtRepo: FakeDebtRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeTransactionRepo = FakeTransactionRepository()
        fakeDebtRepo = FakeDebtRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `linked DEBT transaction resolves debt with correct type`() = runTest {
        val transactionId = UUID.randomUUID()
        fakeTransactionRepo.transactions[transactionId] = Transaction(
            id = transactionId,
            category = testCategory,
            wallet = testWallet,
            type = TransactionType.INCOME,
            amount = 1000.0,
            date = LocalDateTime.of(2026, 1, 1, 10, 0),
            note = "Borrowed money"
        )
        fakeDebtRepo.debtsByTransaction[transactionId] = Debt(
            name = "Borrowed money",
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            walletId = testWallet.id,
            transactionId = transactionId
        )

        val (viewModel, _, _) = createViewModel(transactionId, isTransfer = false)
        viewModel.onEvent(TransactionDetailEvent.InitData)
        advanceUntilIdle()

        val linkedDebt = viewModel.state.value.linkedDebt
        assertEquals(DebtType.DEBT, linkedDebt?.type)
        assertEquals(transactionId, linkedDebt?.transactionId)
    }

    @Test
    fun `linked RECEIVABLE transaction resolves debt with correct type`() = runTest {
        val transactionId = UUID.randomUUID()
        fakeTransactionRepo.transactions[transactionId] = Transaction(
            id = transactionId,
            category = testCategory,
            wallet = testWallet,
            type = TransactionType.EXPENSE,
            amount = 500.0,
            date = LocalDateTime.of(2026, 1, 2, 10, 0),
            note = "Lent money"
        )
        fakeDebtRepo.debtsByTransaction[transactionId] = Debt(
            name = "Lent money",
            type = DebtType.RECEIVABLE,
            totalAmount = 500.0,
            walletId = testWallet.id,
            transactionId = transactionId
        )

        val (viewModel, _, _) = createViewModel(transactionId, isTransfer = false)
        viewModel.onEvent(TransactionDetailEvent.InitData)
        advanceUntilIdle()

        assertEquals(DebtType.RECEIVABLE, viewModel.state.value.linkedDebt?.type)
    }

    @Test
    fun `plain transaction has no linked debt`() = runTest {
        val transactionId = UUID.randomUUID()
        fakeTransactionRepo.transactions[transactionId] = Transaction(
            id = transactionId,
            category = testCategory,
            wallet = testWallet,
            type = TransactionType.EXPENSE,
            amount = 100.0,
            date = LocalDateTime.of(2026, 1, 3, 10, 0),
            note = "Snack"
        )

        val (viewModel, _, _) = createViewModel(transactionId, isTransfer = false)
        viewModel.onEvent(TransactionDetailEvent.InitData)
        advanceUntilIdle()

        assertNull(viewModel.state.value.linkedDebt)
        assertEquals(transactionId, viewModel.state.value.transaction?.id)
    }

    @Test
    fun `transfer detail does not resolve debt`() = runTest {
        val transactionId = UUID.randomUUID()
        fakeTransactionRepo.transferDetail = TransferDetail(
            referenceId = UUID.randomUUID(),
            sourceWallet = testWallet,
            targetWallet = testWallet,
            amount = 200.0,
            fee = 0.0,
            date = LocalDateTime.of(2026, 1, 4, 10, 0),
            note = null
        )

        val (viewModel, _, _) = createViewModel(transactionId, isTransfer = true)
        viewModel.onEvent(TransactionDetailEvent.InitData)
        advanceUntilIdle()

        assertNull(viewModel.state.value.linkedDebt)
        assertEquals(200.0, viewModel.state.value.transferDetail?.amount ?: 0.0, 0.001)
    }

    @Test
    fun `debt linked detail shows plus action and opens sheet`() = runTest {
        val transactionId = UUID.randomUUID()
        fakeTransactionRepo.transactions[transactionId] = Transaction(
            id = transactionId,
            category = testCategory,
            wallet = testWallet,
            type = TransactionType.INCOME,
            amount = 1000.0,
            date = LocalDateTime.of(2026, 1, 1, 10, 0),
            note = "Borrowed money"
        )
        fakeDebtRepo.debtsByTransaction[transactionId] = Debt(
            name = "Borrowed money",
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            walletId = testWallet.id,
            transactionId = transactionId
        )

        val (viewModel, _, _) = createViewModel(transactionId, isTransfer = false)
        viewModel.onEvent(TransactionDetailEvent.InitData)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.canShowPlus)
        viewModel.onEvent(TransactionDetailEvent.ShowPlusSheet(true))
        assertTrue(viewModel.state.value.showPlusSheet)
    }

    @Test
    fun `transfer detail hides plus action`() = runTest {
        val transactionId = UUID.randomUUID()
        fakeTransactionRepo.transferDetail = TransferDetail(
            referenceId = UUID.randomUUID(),
            sourceWallet = testWallet,
            targetWallet = testWallet,
            amount = 200.0,
            fee = 0.0,
            date = LocalDateTime.of(2026, 1, 4, 10, 0),
            note = null
        )

        val (viewModel, _, _) = createViewModel(transactionId, isTransfer = true)
        viewModel.onEvent(TransactionDetailEvent.InitData)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.canShowPlus)
        viewModel.onEvent(TransactionDetailEvent.ShowPlusSheet(true))
        assertFalse(viewModel.state.value.showPlusSheet)
    }

    @Test
    fun `plain transaction hides plus action`() = runTest {
        val transactionId = UUID.randomUUID()
        fakeTransactionRepo.transactions[transactionId] = Transaction(
            id = transactionId,
            category = testCategory,
            wallet = testWallet,
            type = TransactionType.EXPENSE,
            amount = 100.0,
            date = LocalDateTime.of(2026, 1, 3, 10, 0),
            note = "Snack"
        )

        val (viewModel, _, _) = createViewModel(transactionId, isTransfer = false)
        viewModel.onEvent(TransactionDetailEvent.InitData)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.canShowPlus)
    }

    @Test
    fun `delete with linked debt removes whole debt instead of single transaction`() = runTest {
        val transactionId = UUID.randomUUID()
        fakeTransactionRepo.transactions[transactionId] = Transaction(
            id = transactionId,
            category = testCategory,
            wallet = testWallet,
            type = TransactionType.INCOME,
            amount = 1000.0,
            date = LocalDateTime.of(2026, 1, 1, 10, 0),
            note = "Borrowed money"
        )
        val debt = Debt(
            name = "Borrowed money",
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            walletId = testWallet.id,
            transactionId = transactionId
        )
        fakeDebtRepo.debtsByTransaction[transactionId] = debt

        val (viewModel, transactionUseCases, debtUseCases) =
            createViewModel(transactionId, isTransfer = false)
        viewModel.onEvent(TransactionDetailEvent.InitData)
        advanceUntilIdle()

        viewModel.onEvent(TransactionDetailEvent.DeleteTransaction)
        advanceUntilIdle()

        coVerify { debtUseCases.deleteDebt(debt) }
        coVerify(exactly = 0) { transactionUseCases.deleteTransaction(any()) }
    }

    @Test
    fun `delete plain transaction still deletes single transaction`() = runTest {
        val transactionId = UUID.randomUUID()
        fakeTransactionRepo.transactions[transactionId] = Transaction(
            id = transactionId,
            category = testCategory,
            wallet = testWallet,
            type = TransactionType.EXPENSE,
            amount = 100.0,
            date = LocalDateTime.of(2026, 1, 3, 10, 0),
            note = "Snack"
        )

        val (viewModel, transactionUseCases, debtUseCases) =
            createViewModel(transactionId, isTransfer = false)
        viewModel.onEvent(TransactionDetailEvent.InitData)
        advanceUntilIdle()

        viewModel.onEvent(TransactionDetailEvent.DeleteTransaction)
        advanceUntilIdle()

        coVerify { transactionUseCases.deleteTransaction(transactionId) }
        coVerify(exactly = 0) { debtUseCases.deleteDebt(any()) }
    }

    private fun createViewModel(
        transactionId: UUID,
        isTransfer: Boolean
    ): Triple<TransactionDetailViewModel, TransactionUseCases, DebtUseCases> {
        val transactionUseCases = TransactionUseCases(
            addTransaction = mockk(relaxed = true),
            updateTransaction = mockk(relaxed = true),
            deleteTransaction = mockk(relaxed = true),
            getTransactionById = GetTransactionById(fakeTransactionRepo),
            getAllTransactions = mockk(relaxed = true),
            getTransactions = mockk(relaxed = true),
            addTransfer = mockk(relaxed = true),
            updateTransfer = mockk(relaxed = true),
            getTransferDetail = GetTransferDetail(fakeTransactionRepo),
            saveTransactionImage = mockk(relaxed = true),
            getIncomeSum = mockk(relaxed = true),
            getExpenseSum = mockk(relaxed = true),
            getNetBalance = mockk(relaxed = true),
            getRecentTransactions = mockk(relaxed = true),
            getAllTransactionsPaged = mockk(relaxed = true),
            getTransactionsPaged = mockk(relaxed = true)
        )
        val debtUseCases = DebtUseCases(
            getAllDebts = mockk(relaxed = true),
            getDebtById = mockk(relaxed = true),
            getDebtByTransactionId = GetDebtByTransactionId(fakeDebtRepo),
            upsertDebt = mockk(relaxed = true),
            deleteDebt = mockk(relaxed = true),
            recordDebtPayment = mockk(relaxed = true),
            increaseDebtAmount = mockk(relaxed = true),
            getDebtHistory = mockk(relaxed = true)
        )
        val handle = SavedStateHandle(
            mapOf(
                Screen.TransactionDetail.TRANSACTION_ID to transactionId.toString(),
                Screen.TransactionDetail.IS_TRANSFER to isTransfer
            )
        )
        return Triple(
            TransactionDetailViewModel(
                transactionUseCases = transactionUseCases,
                debtUseCases = debtUseCases,
                handle = handle,
                application = mockk<Application>(relaxed = true)
            ),
            transactionUseCases,
            debtUseCases
        )
    }

    class FakeTransactionRepository : TransactionRepository {
        val transactions = mutableMapOf<UUID, Transaction>()
        var transferDetail: TransferDetail? = null

        override fun getAllTransactions(
            startDateRange: Long,
            endDateRange: Long,
            categories: Set<UUID>?,
            wallets: Set<UUID>?
        ): Flow<List<Transaction>> = flowOf(emptyList())

        override fun getAllTransactionsPaged(
            startDateRange: Long,
            endDateRange: Long,
            categories: Set<UUID>?,
            wallets: Set<UUID>?
        ): Flow<PagingData<Transaction>> = flowOf(PagingData.empty())

        override fun getRecentTransactions(limit: Int): Flow<List<Transaction>> =
            flowOf(emptyList())

        override fun getIncomeSum(
            startDateRange: Long,
            endDateRange: Long,
            categories: Set<UUID>?,
            wallets: Set<UUID>?
        ): Flow<Double> = flowOf(0.0)

        override fun getExpenseSum(
            startDateRange: Long,
            endDateRange: Long,
            categories: Set<UUID>?,
            wallets: Set<UUID>?
        ): Flow<Double> = flowOf(0.0)

        override fun getNetBalance(
            startDateRange: Long,
            endDateRange: Long,
            categories: Set<UUID>?,
            wallets: Set<UUID>?
        ): Flow<Double> = flowOf(0.0)

        override fun getAllTransactions(query: String): Flow<List<Transaction>> =
            flowOf(emptyList())

        override fun getAllTransactionsPaged(query: String): Flow<PagingData<Transaction>> =
            flowOf(PagingData.empty())

        override fun getTransactionsByWallet(walletId: UUID): Flow<List<Transaction>> =
            flowOf(emptyList())

        override fun getTransactionsByDebtId(debtId: UUID): Flow<List<Transaction>> =
            flowOf(emptyList())

        override suspend fun getTransactionById(id: UUID): Transaction? = transactions[id]

        override suspend fun getTransactionCountByRecurringId(recurringId: UUID): Int = 0

        override suspend fun addIncomeOrExpense(
            amount: Double,
            type: TransactionType,
            date: LocalDateTime,
            note: String?,
            walletId: UUID,
            categoryId: UUID?,
            recurringTransactionId: UUID?,
            debtId: UUID?
        ): UUID = UUID.randomUUID()

        override suspend fun updateIncomeOrExpense(
            id: UUID,
            amount: Double,
            type: TransactionType,
            date: LocalDateTime,
            note: String?,
            walletId: UUID,
            categoryId: UUID?,
            debtId: UUID?
        ) {
        }

        override suspend fun transferFunds(
            sourceWalletId: UUID,
            targetWalletId: UUID,
            amount: Double,
            fee: Double,
            date: LocalDateTime,
            note: String?
        ) {
        }

        override suspend fun updateTransfer(
            referenceId: UUID,
            sourceWalletId: UUID,
            targetWalletId: UUID,
            amount: Double,
            fee: Double,
            date: LocalDateTime,
            note: String?
        ) {
        }

        override suspend fun getTransferDetail(transactionId: UUID): TransferDetail? =
            transferDetail

        override suspend fun deleteTransaction(transactionId: UUID) {}
    }

    class FakeDebtRepository : DebtRepository {
        val debtsByTransaction = mutableMapOf<UUID, Debt>()

        override fun getAllDebts(): Flow<List<Debt>> = flowOf(emptyList())

        override suspend fun getDebtById(id: UUID): Debt? = null

        override suspend fun getDebtByTransactionId(transactionId: UUID): Debt? =
            debtsByTransaction[transactionId]

        override suspend fun getDebtsByWalletId(walletId: UUID): List<Debt> = emptyList()

        override suspend fun upsertDebt(debt: Debt) {}

        override suspend fun deleteDebt(debt: Debt) {}
    }
}
