package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

import dev.muffar.moneyfikasi.domain.repository.UiSettingsRepository

class IsBalanceVisible(
    private val uiSettingsRepository: UiSettingsRepository
) {
    operator fun invoke() = uiSettingsRepository.isBalanceVisible()
}