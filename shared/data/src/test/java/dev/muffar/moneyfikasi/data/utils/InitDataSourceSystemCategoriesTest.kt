package dev.muffar.moneyfikasi.data.utils

import android.content.Context
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.constants.UUIDConst
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class InitDataSourceSystemCategoriesTest {

    private val context: Context = mockk()

    @Before
    fun setUp() {
        every { context.getString(any()) } answers {
            when (firstArg<Int>()) {
                R.string.cat_debt -> "Hutang"
                R.string.cat_receivable -> "Piutang"
                else -> "Other"
            }
        }
    }

    @Test
    fun `system categories include debt category with localized name`() {
        val debt = InitDataSource.getSystemCategories(context)
            .first { it.id == UUIDConst.DebtCategoryId }

        assertEquals("Hutang", debt.name)
        assertEquals(CategoryType.EXPENSE, debt.type)
        assertFalse(debt.isActive)
    }

    @Test
    fun `system categories include receivable category with localized name`() {
        val receivable = InitDataSource.getSystemCategories(context)
            .first { it.id == UUIDConst.ReceivableCategoryId }

        assertEquals("Piutang", receivable.name)
        assertEquals(CategoryType.INCOME, receivable.type)
        assertFalse(receivable.isActive)
    }

    @Test
    fun `system categories keep regular categories and add debt ones`() {
        val regular = InitDataSource.getCategories(context)
        val system = InitDataSource.getSystemCategories(context)

        assertEquals(regular.size + 2, system.size)
        assertEquals(
            regular.map { it.name }.toSet() + setOf("Hutang", "Piutang"),
            system.map { it.name }.toSet()
        )
    }
}
