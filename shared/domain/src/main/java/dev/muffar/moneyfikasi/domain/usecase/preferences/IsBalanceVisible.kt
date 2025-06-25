package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class IsBalanceVisible(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke() = preferencesRepository.isBalanceVisible()
}