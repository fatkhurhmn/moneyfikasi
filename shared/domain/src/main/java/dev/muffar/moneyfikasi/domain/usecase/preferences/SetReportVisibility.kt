package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class SetReportVisibility(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(isVisible: Boolean) =
        preferencesRepository.setReportVisibility(isVisible)
}