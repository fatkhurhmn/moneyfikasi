package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.AppIcon
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.Colors
import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import dev.muffar.moneyfikasi.utils.constants.UUIDConst
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.UUID

class UpsertDebtTypeTest {

    private val debtRepository: DebtRepository = mockk(relaxed = true)
    private val transactionRepository: TransactionRepository = mockk(relaxed = true)
    private val categoryRepository: CategoryRepository = mockk(relaxed = true)

    private lateinit var upsertDebt: UpsertDebt

    private val walletId = UUID.randomUUID()

    @Before
    fun setUp() {
        upsertDebt = UpsertDebt(
            debtRepository = debtRepository,
            transactionRepository = transactionRepository,
            categoryRepository = categoryRepository
        )
        coEvery { debtRepository.getDebtById(any()) } returns null
        coEvery {
            transactionRepository.addIncomeOrExpense(any(), any(), any(), any(), any(), any())
        } returns UUID.randomUUID()
        coEvery { categoryRepository.getCategoryById(UUIDConst.DebtCategoryId) } returns Category(
            id = UUIDConst.DebtCategoryId,
            name = "Debt",
            icon = AppIcon.Receipt.name,
            color = Colors.Green10.color,
            type = CategoryType.INCOME,
            isActive = false
        )
        coEvery { categoryRepository.getCategoryById(UUIDConst.ReceivableCategoryId) } returns Category(
            id = UUIDConst.ReceivableCategoryId,
            name = "Receivable",
            icon = AppIcon.Receipt.name,
            color = Colors.Red20.color,
            type = CategoryType.EXPENSE,
            isActive = false
        )
    }

    @Test
    fun `DEBT creates INCOME linked transaction with debt category`() = runTest {
        val debt = Debt(
            name = "Borrowed money",
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            walletId = walletId
        )

        upsertDebt(debt)

        coVerify {
            transactionRepository.addIncomeOrExpense(
                amount = 1000.0,
                type = TransactionType.INCOME,
                date = any(),
                note = "Borrowed money",
                walletId = walletId,
                categoryId = UUIDConst.DebtCategoryId
            )
        }
    }

    @Test
    fun `RECEIVABLE creates EXPENSE linked transaction with receivable category`() = runTest {
        val debt = Debt(
            name = "Lent money",
            type = DebtType.RECEIVABLE,
            totalAmount = 1000.0,
            walletId = walletId
        )

        upsertDebt(debt)

        coVerify {
            transactionRepository.addIncomeOrExpense(
                amount = 1000.0,
                type = TransactionType.EXPENSE,
                date = any(),
                note = "Lent money",
                walletId = walletId,
                categoryId = UUIDConst.ReceivableCategoryId
            )
        }
    }

    @Test
    fun `edit changing type moves linked transaction category and corrects balance`() = runTest {
        val debtId = UUID.randomUUID()
        val transactionId = UUID.randomUUID()
        val existing = Debt(
            id = debtId,
            name = "Lent money",
            type = DebtType.RECEIVABLE,
            totalAmount = 1000.0,
            walletId = walletId,
            transactionId = transactionId
        )
        coEvery { debtRepository.getDebtById(debtId) } returns existing

        upsertDebt(existing.copy(type = DebtType.DEBT))

        coVerify {
            transactionRepository.updateIncomeOrExpense(
                id = transactionId,
                amount = 1000.0,
                type = TransactionType.INCOME,
                date = any(),
                note = "Lent money",
                walletId = walletId,
                categoryId = UUIDConst.DebtCategoryId
            )
        }
        coVerify { debtRepository.upsertDebt(match { it.transactionId == transactionId }) }
    }

    @Test
    fun `missing system categories are created`() = runTest {
        coEvery { categoryRepository.getCategoryById(any()) } returns null
        val debt = Debt(
            name = "Borrowed money",
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            walletId = walletId
        )

        upsertDebt(debt)

        coVerify {
            categoryRepository.upsertCategory(
                match {
                    it.id == UUIDConst.DebtCategoryId &&
                        it.type == CategoryType.INCOME &&
                        it.color == Colors.Green10.color &&
                        !it.isActive
                }
            )
        }
        coVerify {
            categoryRepository.upsertCategory(
                match {
                    it.id == UUIDConst.ReceivableCategoryId &&
                        it.type == CategoryType.EXPENSE &&
                        it.color == Colors.Red20.color &&
                        !it.isActive
                }
            )
        }
    }

    @Test
    fun `legacy swapped debt categories are normalized without touching name`() = runTest {
        coEvery { categoryRepository.getCategoryById(UUIDConst.DebtCategoryId) } returns Category(
            id = UUIDConst.DebtCategoryId,
            name = "Hutang",
            icon = AppIcon.Receipt.name,
            color = Colors.Red20.color,
            type = CategoryType.EXPENSE,
            isActive = false
        )
        coEvery { categoryRepository.getCategoryById(UUIDConst.ReceivableCategoryId) } returns Category(
            id = UUIDConst.ReceivableCategoryId,
            name = "Piutang",
            icon = AppIcon.Receipt.name,
            color = Colors.Green10.color,
            type = CategoryType.INCOME,
            isActive = false
        )
        val debt = Debt(
            name = "Borrowed money",
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            walletId = walletId
        )

        upsertDebt(debt)

        coVerify {
            categoryRepository.upsertCategory(
                match {
                    it.id == UUIDConst.DebtCategoryId &&
                        it.name == "Hutang" &&
                        it.type == CategoryType.INCOME &&
                        it.color == Colors.Green10.color &&
                        it.icon == AppIcon.Receipt.name
                }
            )
        }
        coVerify {
            categoryRepository.upsertCategory(
                match {
                    it.id == UUIDConst.ReceivableCategoryId &&
                        it.name == "Piutang" &&
                        it.type == CategoryType.EXPENSE &&
                        it.color == Colors.Red20.color &&
                        it.icon == AppIcon.Receipt.name
                }
            )
        }
    }

    @Test
    fun `existing system categories are not recreated`() = runTest {
        val debt = Debt(
            name = "Borrowed money",
            type = DebtType.DEBT,
            totalAmount = 1000.0,
            walletId = walletId
        )

        upsertDebt(debt)

        coVerify(exactly = 0) { categoryRepository.upsertCategory(any<Category>()) }
    }
}
