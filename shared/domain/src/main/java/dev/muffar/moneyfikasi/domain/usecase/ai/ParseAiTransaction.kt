package dev.muffar.moneyfikasi.domain.usecase.ai

import dev.muffar.moneyfikasi.domain.model.AiTransactionResult
import dev.muffar.moneyfikasi.domain.repository.AiRepository

class ParseAiTransaction(
    private val aiRepository: AiRepository
) {
    suspend operator fun invoke(input: String): AiTransactionResult? {
        return aiRepository.parseTransaction(input)
    }
}
