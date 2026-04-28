package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.UiPreferencesRepository

class SetReportVisibility(
    private val uiPreferencesRepository: UiPreferencesRepository
) {
    suspend operator fun invoke(isVisible: Boolean) = uiPreferencesRepository.setReportVisibility(isVisible)
}