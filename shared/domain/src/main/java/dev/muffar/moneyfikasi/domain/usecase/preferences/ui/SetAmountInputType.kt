package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

import dev.muffar.moneyfikasi.domain.model.AmountInputType
import dev.muffar.moneyfikasi.domain.repository.UiSettingsRepository

class SetAmountInputType(
    private val uiSettingsRepository: UiSettingsRepository
) {
    suspend operator fun invoke(type: AmountInputType) {
        uiSettingsRepository.setAmountInputType(type)
    }
}
