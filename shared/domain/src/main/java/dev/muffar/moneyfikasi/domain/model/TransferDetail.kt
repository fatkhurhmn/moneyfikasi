package dev.muffar.moneyfikasi.domain.model

import org.threeten.bp.LocalDateTime
import java.util.UUID

data class TransferDetail(
    val referenceId: UUID,
    val sourceWallet: Wallet,
    val targetWallet: Wallet,
    val amount: Double,
    val fee: Double,
    val date: LocalDateTime,
    val note: String?
)
