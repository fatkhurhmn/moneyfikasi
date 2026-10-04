package dev.muffar.moneyfikasi.transaction.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.muffar.moneyfikasi.domain.model.AmountInputType
import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.UiSettings
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import dev.muffar.moneyfikasi.domain.repository.UiSettingsRepository
import dev.muffar.moneyfikasi.domain.repository.WalletRepository
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.category.GetAllCategories
import dev.muffar.moneyfikasi.domain.usecase.category.GetCategoryById
import dev.muffar.moneyfikasi.domain.usecase.category.GetCategoryByType
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.GetUiSettings
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.SetTransactionCalendarMode
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.UiSettingsUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.GetAllTransactions
import dev.muffar.moneyfikasi.domain.usecase.transaction.GetAllTransactionsPaged
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.GetAllWallets
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfMonth
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfMonth
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.toMilliseconds
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionsViewModelCalendarTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var fakeTransactionRepo: FakeTransactionRepository
    private lateinit var fakeUiSettingsRepo: FakeUiSettingsRepository
    private lateinit var viewModel: TransactionsViewModel

    private val testCategory = Category(
        id = UUID.randomUUID(),
        name = "Food",
        type = CategoryType.EXPENSE,
        isActive = true
    )
    private val testWallet = Wallet(id = UUID.randomUUID(), name = "Main", balance = 1000.0)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeTransactionRepo = FakeTransactionRepository()
        fakeUiSettingsRepo = FakeUiSettingsRepository()

        val transactionUseCases = TransactionUseCases(
            addTransaction = mockk(relaxed = true),
            updateTransaction = mockk(relaxed = true),
            deleteTransaction = mockk(relaxed = true),
            getTransactionById = mockk(relaxed = true),
            getAllTransactions = GetAllTransactions(fakeTransactionRepo),
            getTransactions = mockk(relaxed = true),
            addTransfer = mockk(relaxed = true),
            updateTransfer = mockk(relaxed = true),
            getTransferDetail = mockk(relaxed = true),
            saveTransactionImage = mockk(relaxed = true),
            getIncomeSum = mockk(relaxed = true),
            getExpenseSum = mockk(relaxed = true),
            getNetBalance = mockk(relaxed = true),
            getRecentTransactions = mockk(relaxed = true),
            getAllTransactionsPaged = GetAllTransactionsPaged(fakeTransactionRepo),
            getTransactionsPaged = mockk(relaxed = true)
        )

        val categoryRepo = mockk<CategoryRepository>()
        every { categoryRepo.getAllCategories(any()) } returns flowOf(listOf(testCategory))
        every {
            categoryRepo.getCategoriesByType(
                any(),
                any()
            )
        } returns flowOf(listOf(testCategory))
        val categoryUseCases = CategoryUseCases(
            upsertCategory = mockk(relaxed = true),
            deleteCategory = mockk(relaxed = true),
            getAllCategories = GetAllCategories(categoryRepo),
            getCategoryById = GetCategoryById(categoryRepo),
            getCategoryByType = GetCategoryByType(categoryRepo),
            updateDefaultCategories = mockk(relaxed = true)
        )

        val walletRepo = mockk<WalletRepository>()
        every { walletRepo.getAllWallets() } returns flowOf(listOf(testWallet))
        val walletUseCases = WalletUseCases(
            upsertWallet = mockk(relaxed = true),
            deleteWallet = mockk(relaxed = true),
            getAllWallets = GetAllWallets(walletRepo),
            getWalletById = mockk(relaxed = true)
        )

        val uiSettingsUseCases = UiSettingsUseCases(
            getUiSettings = GetUiSettings(fakeUiSettingsRepo),
            setBalanceVisibility = mockk(relaxed = true),
            setReportVisibility = mockk(relaxed = true),
            setQuickTransactionVisibility = mockk(relaxed = true),
            setBudgetVisibility = mockk(relaxed = true),
            setAppTheme = mockk(relaxed = true),
            setAppLanguage = mockk(relaxed = true),
            setAmountInputType = mockk(relaxed = true),
            setBudgetCutoffDay = mockk(relaxed = true),
            setTransactionCalendarMode = SetTransactionCalendarMode(fakeUiSettingsRepo)
        )

        viewModel = TransactionsViewModel(
            transactionUseCases = transactionUseCases,
            categoryUseCases = categoryUseCases,
            walletUseCases = walletUseCases,
            uiSettingsUseCases = uiSettingsUseCases
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is list mode`() = runTest {
        advanceUntilIdle()
        assertFalse(viewModel.state.value.isCalendarMode)
        assertEquals(0, viewModel.state.value.calendarDailyBalances.size)
    }

    @Test
    fun `toggle calendar mode persists and updates state`() = runTest {
        advanceUntilIdle()
        assertFalse(viewModel.state.value.isCalendarMode)

        viewModel.onEvent(TransactionsEvent.ToggleCalendarMode)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.isCalendarMode)
        assertTrue(fakeUiSettingsRepo.lastCalendarMode == true)

        viewModel.onEvent(TransactionsEvent.ToggleCalendarMode)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.isCalendarMode)
        assertTrue(fakeUiSettingsRepo.lastCalendarMode == false)
    }

    @Test
    fun `calendar mode loads data for current month (30 days period)`() = runTest {
        // Use current month from ViewModel to be deterministic
        val month = viewModel.state.value.calendarMonth
        val txs = listOf(
            transactionOn(month.year, month.monthValue, 1, 100.0, TransactionType.EXPENSE),
            transactionOn(month.year, month.monthValue, 1, 50.0, TransactionType.INCOME),
            transactionOn(month.year, month.monthValue, 15, 200.0, TransactionType.EXPENSE)
        )
        fakeTransactionRepo.transactions = txs

        viewModel.onEvent(TransactionsEvent.ToggleCalendarMode)
        advanceUntilIdle()

        // Should auto-select today
        val today = LocalDateTime.now().dayOfMonth
        if (month.monthValue == LocalDateTime.now().monthValue) {
            assertEquals(today, viewModel.state.value.calendarSelectedDay)
        }

        val balances = viewModel.state.value.calendarDailyBalances
        assertEquals(-50.0, balances[1] ?: 0.0, 0.001)
        assertEquals(-200.0, balances[15] ?: 0.0, 0.001)
        assertTrue(balances[2] == null)
    }

    @Test
    fun `calendar previous and next month change month and reload`() = runTest {
        fakeTransactionRepo.transactions = emptyList()
        viewModel.onEvent(TransactionsEvent.ToggleCalendarMode)
        advanceUntilIdle()
        val initialMonth = viewModel.state.value.calendarMonth.monthValue

        viewModel.onEvent(TransactionsEvent.CalendarPreviousMonth)
        advanceUntilIdle()
        // handle year wrap: just check month changed
        assertTrue(viewModel.state.value.calendarMonth.monthValue != initialMonth || viewModel.state.value.calendarMonth.year != LocalDateTime.now().year)

        viewModel.onEvent(TransactionsEvent.CalendarNextMonth)
        advanceUntilIdle()
        assertEquals(initialMonth, viewModel.state.value.calendarMonth.monthValue)
    }

    @Test
    fun `selecting day loads transactions for that day and stays in calendar mode`() = runTest {
        val month = viewModel.state.value.calendarMonth
        val day15Tx = transactionOn(month.year, month.monthValue, 15, 75.0, TransactionType.EXPENSE)
        fakeTransactionRepo.transactions = listOf(day15Tx)

        viewModel.onEvent(TransactionsEvent.ToggleCalendarMode)
        advanceUntilIdle()

        viewModel.onEvent(TransactionsEvent.CalendarDaySelected(15))
        advanceUntilIdle()

        assertEquals(15, viewModel.state.value.calendarSelectedDay)
        assertTrue(viewModel.state.value.isCalendarMode)
        assertEquals(1, viewModel.state.value.calendarSelectedDayTransactions.size)

        viewModel.onEvent(TransactionsEvent.CalendarDaySelected(15))
        advanceUntilIdle()
        assertEquals(null, viewModel.state.value.calendarSelectedDay)
        assertTrue(viewModel.state.value.calendarSelectedDayTransactions.isEmpty())
    }

    @Test
    fun `balance calculation is net income minus expense`() = runTest {
        val month = viewModel.state.value.calendarMonth
        val txs = listOf(
            transactionOn(month.year, month.monthValue, 5, 1000.0, TransactionType.INCOME),
            transactionOn(month.year, month.monthValue, 5, 300.0, TransactionType.EXPENSE),
            transactionOn(month.year, month.monthValue, 5, 200.0, TransactionType.TRANSFER_IN),
            transactionOn(month.year, month.monthValue, 5, 100.0, TransactionType.TRANSFER_OUT)
        )
        fakeTransactionRepo.transactions = txs
        viewModel.onEvent(TransactionsEvent.ToggleCalendarMode)
        advanceUntilIdle()
        assertEquals(800.0, viewModel.state.value.calendarDailyBalances[5] ?: 0.0, 0.001)
    }

    private fun transactionOn(
        year: Int,
        month: Int,
        day: Int,
        amount: Double,
        type: TransactionType
    ): Transaction {
        return Transaction(
            id = UUID.randomUUID(),
            category = testCategory,
            wallet = testWallet,
            type = type,
            amount = amount,
            date = LocalDateTime.of(year, month, day, 10, 0),
            note = null
        )
    }

    class FakeTransactionRepository : TransactionRepository {
        var transactions: List<Transaction> = emptyList()
        var lastStart: Long? = null
        var lastEnd: Long? = null

        override fun getAllTransactions(
            startDateRange: Long,
            endDateRange: Long,
            categories: Set<UUID>?,
            wallets: Set<UUID>?
        ): Flow<List<Transaction>> {
            lastStart = startDateRange
            lastEnd = endDateRange
            return flowOf(transactions.filter { it.date.toMilliseconds() in startDateRange..endDateRange })
        }

        override fun getAllTransactionsPaged(
            startDateRange: Long,
            endDateRange: Long,
            categories: Set<UUID>?,
            wallets: Set<UUID>?
        ): Flow<androidx.paging.PagingData<Transaction>> {
            return flowOf(androidx.paging.PagingData.from(transactions))
        }

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

        override fun getAllTransactionsPaged(query: String): Flow<androidx.paging.PagingData<Transaction>> =
            flowOf(androidx.paging.PagingData.empty())

        override fun getTransactionsByWallet(walletId: UUID): Flow<List<Transaction>> =
            flowOf(emptyList())

        override suspend fun getTransactionById(id: UUID): Transaction? = null
        override suspend fun getTransactionCountByRecurringId(recurringId: UUID): Int = 0
        override suspend fun addIncomeOrExpense(
            amount: Double,
            type: TransactionType,
            date: LocalDateTime,
            note: String?,
            walletId: UUID,
            categoryId: UUID?,
            recurringTransactionId: UUID?
        ): UUID = UUID.randomUUID()

        override suspend fun updateIncomeOrExpense(
            id: UUID,
            amount: Double,
            type: TransactionType,
            date: LocalDateTime,
            note: String?,
            walletId: UUID,
            categoryId: UUID?
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

        override suspend fun getTransferDetail(transactionId: UUID): dev.muffar.moneyfikasi.domain.model.TransferDetail? =
            null

        override suspend fun deleteTransaction(transactionId: UUID) {}
    }

    class FakeUiSettingsRepository : UiSettingsRepository {
        private val flow = MutableStateFlow(UiSettings())
        var lastCalendarMode: Boolean? = null

        override fun getUiSettings(): Flow<UiSettings> = flow

        override suspend fun setBalanceVisibility(isVisible: Boolean) {
            flow.value = flow.value.copy(isBalanceVisible = isVisible)
        }

        override suspend fun setReportVisibility(isVisible: Boolean) {
            flow.value = flow.value.copy(isReportVisible = isVisible)
        }

        override suspend fun setQuickTransactionVisibility(isVisible: Boolean) {
            flow.value = flow.value.copy(isQuickTransactionVisible = isVisible)
        }

        override suspend fun setBudgetVisibility(isVisible: Boolean) {
            flow.value = flow.value.copy(isBudgetVisible = isVisible)
        }

        override suspend fun setAppTheme(theme: AppTheme) {
            flow.value = flow.value.copy(appTheme = theme)
        }

        override suspend fun setAppLanguage(language: AppLanguage) {
            flow.value = flow.value.copy(appLanguage = language)
        }

        override suspend fun setAmountInputType(type: AmountInputType) {
            flow.value = flow.value.copy(amountInputType = type)
        }

        override suspend fun setBudgetCutoffDay(day: Int) {
            flow.value = flow.value.copy(budgetCutoffDay = day)
        }

        override suspend fun setTransactionCalendarMode(isCalendarMode: Boolean) {
            lastCalendarMode = isCalendarMode
            flow.value = flow.value.copy(isTransactionCalendarMode = isCalendarMode)
        }
    }
}
