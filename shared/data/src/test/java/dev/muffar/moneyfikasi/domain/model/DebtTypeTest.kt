package dev.muffar.moneyfikasi.domain.model

import dev.muffar.moneyfikasi.data.db.Converters
import org.junit.Assert.assertEquals
import org.junit.Test

class DebtTypeTest {

    private val converters = Converters()

    @Test
    fun `fromString resolves new and legacy route values`() {
        assertEquals(DebtType.DEBT, DebtType.fromString("debt"))
        assertEquals(DebtType.RECEIVABLE, DebtType.fromString("receivable"))
        assertEquals(DebtType.DEBT, DebtType.fromString("hutang"))
        assertEquals(DebtType.RECEIVABLE, DebtType.fromString("piutang"))
    }

    @Test
    fun `fromString and converter fall back to DEBT on unknown value`() {
        assertEquals(DebtType.DEBT, DebtType.fromString("unknown"))
        assertEquals(DebtType.DEBT, converters.toDebtType("HUTANG"))
        assertEquals(DebtType.RECEIVABLE, converters.toDebtType("PIUTANG"))
        assertEquals(DebtType.DEBT, converters.toDebtType("unknown"))
    }
}
