package dev.muffar.moneyfikasi.data.mapper

import dev.muffar.moneyfikasi.data.db.entity.DebtEntity
import dev.muffar.moneyfikasi.data.db.entity.TransactionEntity
import dev.muffar.moneyfikasi.data.db.entity.TransactionWithDetails
import dev.muffar.moneyfikasi.data.db.entity.WalletEntity
import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.utils.constants.UUIDConst
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.threeten.bp.LocalDateTime
import java.util.UUID

class DebtMapperTest {

    @Test
    fun `toDomain maps walletId and transactionId`() {
        val walletId = UUID.randomUUID()
        val transactionId = UUID.randomUUID()
        val entity = DebtEntity(
            name = "Test debt",
            totalAmount = 1000.0,
            paidAmount = 200.0,
            walletId = walletId,
            transactionId = transactionId,
            createdAt = 1L,
            updatedAt = 2L
        )

        val domain = entity.toDomain()

        assertEquals(walletId, domain.walletId)
        assertEquals(transactionId, domain.transactionId)
    }

    @Test
    fun `toDomain falls back to empty walletId when null`() {
        val entity = DebtEntity(
            name = "Legacy debt",
            totalAmount = 500.0,
            walletId = null,
            transactionId = null,
            createdAt = 1L,
            updatedAt = 2L
        )

        val domain = entity.toDomain()

        assertEquals(UUIDConst.empty, domain.walletId)
        assertNull(domain.transactionId)
    }

    @Test
    fun `toDomain maps debt type`() {
        val entity = DebtEntity(
            name = "Lent money",
            type = DebtType.RECEIVABLE,
            totalAmount = 1000.0,
            createdAt = 1L,
            updatedAt = 2L
        )

        val domain = entity.toDomain()

        assertEquals(DebtType.RECEIVABLE, domain.type)
    }

    @Test
    fun `toDomain defaults legacy debt to DEBT`() {
        val entity = DebtEntity(
            name = "Legacy debt",
            totalAmount = 500.0,
            createdAt = 1L,
            updatedAt = 2L
        )

        val domain = entity.toDomain()

        assertEquals(DebtType.DEBT, domain.type)
    }

    @Test
    fun `toEntity round-trips debt type`() {
        val debt = Debt(
            name = "Lent money",
            type = DebtType.RECEIVABLE,
            totalAmount = 1000.0
        )

        val entity = debt.toEntity()

        assertEquals(DebtType.RECEIVABLE, entity.type)
    }

    @Test
    fun `toEntity round-trips walletId and transactionId`() {
        val walletId = UUID.randomUUID()
        val transactionId = UUID.randomUUID()
        val debt = Debt(
            name = "Test debt",
            totalAmount = 1000.0,
            walletId = walletId,
            transactionId = transactionId
        )

        val entity = debt.toEntity()

        assertEquals(walletId, entity.walletId)
        assertEquals(transactionId, entity.transactionId)
    }

    @Test
    fun `transaction with missing wallet falls back to Unknown`() {
        val details = TransactionWithDetails(
            transaction = TransactionEntity(
                walletId = UUID.randomUUID(),
                categoryId = null,
                type = TransactionType.EXPENSE,
                amount = 100.0,
                date = LocalDateTime.of(2026, 1, 1, 0, 0)
            ),
            wallet = null,
            category = null
        )

        val domain = details.toDomain()

        assertEquals(UUIDConst.empty, domain.wallet.id)
        assertEquals("Unknown", domain.wallet.name)
    }

    @Test
    fun `transaction with present wallet keeps wallet`() {
        val walletId = UUID.randomUUID()
        val details = TransactionWithDetails(
            transaction = TransactionEntity(
                walletId = walletId,
                categoryId = null,
                type = TransactionType.EXPENSE,
                amount = 100.0,
                date = LocalDateTime.of(2026, 1, 1, 0, 0)
            ),
            wallet = WalletEntity(
                id = walletId,
                name = "Main",
                icon = "",
                color = 0L,
                balance = 0.0,
                isActive = true
            ),
            category = null
        )

        val domain = details.toDomain()

        assertEquals(walletId, domain.wallet.id)
        assertEquals("Main", domain.wallet.name)
    }
}
