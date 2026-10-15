package dev.muffar.moneyfikasi.transaction.detail.plus

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import dev.muffar.moneyfikasi.domain.repository.WalletRepository
import dev.muffar.moneyfikasi.domain.usecase.debt.DebtUseCases
import dev.muffar.moneyfikasi.domain.usecase.debt.GetDebtById
import dev.muffar.moneyfikasi.domain.usecase.wallet.GetAllWallets
import dev.muffar.moneyfikasi.domain.usecase.wallet.GetWalletById
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.resource.R
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class DebtPlusViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val walletA = Wallet(id = UUID.randomUUID(), name = "Main")
    private val walletB = Wallet(id = UUID.randomUUID(), name = "Cash")

    private lateinit var fakeDebtRepo: FakeDebtRepository
    private lateinit var fakeWalletRepo: FakeWalletRepository
    private lateinit var debtUseCases: DebtUseCases
    private lateinit var walletUseCases: WalletUseCases

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeDebtRepo = FakeDebtRepository()
        fakeWalletRepo = FakeWalletRepository(listOf(walletA, walletB))
        debtUseCases = DebtUseCases(
            getAllDebts = mockk(relaxed = true),
            getDebtById = GetDebtById(fakeDebtRepo),
            getDebtByTransactionId = mockk(relaxed = true),
            upsertDebt = mockk(relaxed = true),
            deleteDebt = mockk(relaxed = true),
            recordDebtPayment = mockk(relaxed = true),
            increaseDebtAmount = mockk(relaxed = true),
            getDebtHistory = mockk(relaxed = true)
        )
        walletUseCases = WalletUseCases(
            getAllWallets = GetAllWallets(fakeWalletRepo),
            getWalletById = GetWalletById(fakeWalletRepo),
            upsertWallet = mockk(relaxed = true),
            deleteWallet = mockk(relaxed = true)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `DEBT tab 1 is payment and tab 0 is increase`() = runTest {
        val debtId = UUID.randomUUID()
        fakeDebtRepo.debts[debtId] = Debt(
            id = debtId,
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            walletId = walletA.id
        )

        val viewModel = createViewModel(debtId, tab = 0)
        advanceUntilIdle()
        assertTrue(!viewModel.state.value.isPaymentTab)

        viewModel.onEvent(DebtPlusEvent.TabSelected(1))
        assertTrue(viewModel.state.value.isPaymentTab)
    }

    @Test
    fun `RECEIVABLE tab 0 is payment and tab 1 is increase`() = runTest {
        val debtId = UUID.randomUUID()
        fakeDebtRepo.debts[debtId] = Debt(
            id = debtId,
            type = DebtType.RECEIVABLE,
            totalAmount = 1000.0,
            walletId = walletA.id
        )

        val viewModel = createViewModel(debtId, tab = 0)
        advanceUntilIdle()
        assertTrue(viewModel.state.value.isPaymentTab)

        viewModel.onEvent(DebtPlusEvent.TabSelected(1))
        assertTrue(!viewModel.state.value.isPaymentTab)
    }

    @Test
    fun `wallet defaults to debt wallet`() = runTest {
        val debtId = UUID.randomUUID()
        fakeDebtRepo.debts[debtId] = Debt(
            id = debtId,
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            walletId = walletB.id
        )

        val viewModel = createViewModel(debtId, tab = 0)
        advanceUntilIdle()

        assertEquals(walletB.id, viewModel.state.value.wallet.id)
    }

    @Test
    fun `overpayment sets error and blocks save`() = runTest {
        val debtId = UUID.randomUUID()
        fakeDebtRepo.debts[debtId] = Debt(
            id = debtId,
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            paidAmount = 900.0,
            walletId = walletA.id
        )

        val viewModel = createViewModel(debtId, tab = 1)
        advanceUntilIdle()

        viewModel.onEvent(DebtPlusEvent.AmountChanged("200"))
        assertEquals(
            R.string.error_paid_exceeds_total,
            viewModel.state.value.amountError.resId
        )

        viewModel.onEvent(DebtPlusEvent.SavePlus)
        advanceUntilIdle()
        coVerify(exactly = 0) { debtUseCases.recordDebtPayment(any(), any(), any(), any()) }
    }

    @Test
    fun `valid payment dispatches with selected wallet and date`() = runTest {
        val debtId = UUID.randomUUID()
        fakeDebtRepo.debts[debtId] = Debt(
            id = debtId,
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            walletId = walletA.id
        )

        val viewModel = createViewModel(debtId, tab = 1)
        advanceUntilIdle()

        viewModel.onEvent(DebtPlusEvent.WalletSelected(walletB))
        viewModel.onEvent(DebtPlusEvent.AmountChanged("300"))
        viewModel.onEvent(DebtPlusEvent.SavePlus)
        advanceUntilIdle()

        coVerify {
            debtUseCases.recordDebtPayment(
                debtId = debtId,
                amount = 300.0,
                walletId = walletB.id,
                date = any()
            )
        }
    }

    @Test
    fun `increase tab dispatches increaseDebtAmount`() = runTest {
        val debtId = UUID.randomUUID()
        fakeDebtRepo.debts[debtId] = Debt(
            id = debtId,
            type = DebtType.RECEIVABLE,
            totalAmount = 1000.0,
            walletId = walletA.id
        )

        val viewModel = createViewModel(debtId, tab = 1)
        advanceUntilIdle()

        viewModel.onEvent(DebtPlusEvent.AmountChanged("250"))
        viewModel.onEvent(DebtPlusEvent.SavePlus)
        advanceUntilIdle()

        coVerify {
            debtUseCases.increaseDebtAmount(
                debtId = debtId,
                amount = 250.0,
                walletId = walletA.id,
                date = any()
            )
        }
    }

    @Test
    fun `missing debt emits NavigateBack`() = runTest {
        val viewModel = createViewModel(UUID.randomUUID(), tab = 0)
        val events = mutableListOf<DebtPlusViewModel.UiEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.eventFlow.collect { events.add(it) }
        }
        advanceUntilIdle()

        assertTrue(events.contains(DebtPlusViewModel.UiEvent.NavigateBack))
    }

    private fun createViewModel(debtId: UUID, tab: Int): DebtPlusViewModel {
        val handle = SavedStateHandle(
            mapOf(
                Screen.DebtPlus.DEBT_ID to debtId.toString(),
                Screen.DebtPlus.TAB to tab
            )
        )
        return DebtPlusViewModel(
            debtUseCases = debtUseCases,
            walletUseCases = walletUseCases,
            handle = handle
        )
    }

    class FakeDebtRepository : DebtRepository {
        val debts = mutableMapOf<UUID, Debt>()

        override fun getAllDebts(): Flow<List<Debt>> = flowOf(debts.values.toList())

        override suspend fun getDebtById(id: UUID): Debt? = debts[id]

        override suspend fun getDebtByTransactionId(transactionId: UUID): Debt? =
            debts.values.find { it.transactionId == transactionId }

        override suspend fun getDebtsByWalletId(walletId: UUID): List<Debt> =
            debts.values.filter { it.walletId == walletId }

        override suspend fun upsertDebt(debt: Debt) {
            debts[debt.id] = debt
        }

        override suspend fun deleteDebt(debt: Debt) {
            debts.remove(debt.id)
        }
    }

    class FakeWalletRepository(
        wallets: List<Wallet>
    ) : WalletRepository {
        private val flow = MutableStateFlow(wallets)

        override fun getAllWallets(): Flow<List<Wallet>> = flow

        override suspend fun getWalletById(id: UUID): Wallet? =
            flow.value.find { it.id == id }

        override suspend fun upsertWallet(wallet: Wallet) {}

        override suspend fun deleteWallet(wallet: Wallet) {}
    }
}
