package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.UiPreferencesRepository

class IsReportVisible(
    private val uiPreferencesRepository: UiPreferencesRepository
) {
    operator fun invoke() = uiPreferencesRepository.isReportVisible()
}