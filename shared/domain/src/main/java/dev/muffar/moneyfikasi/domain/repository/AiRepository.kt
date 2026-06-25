package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.AiTransactionResult

interface AiRepository {
    suspend fun parseTransaction(input: String): Result<AiTransactionResult>
}
