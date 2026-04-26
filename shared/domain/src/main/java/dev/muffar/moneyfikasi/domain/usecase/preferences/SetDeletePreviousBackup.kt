package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class SetDeletePreviousBackup(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(isEnabled: Boolean) {
        preferencesRepository.setDeletePreviousBackup(isEnabled)
    }
}
