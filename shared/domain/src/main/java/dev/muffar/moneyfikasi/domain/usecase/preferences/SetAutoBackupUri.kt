package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class SetAutoBackupUri(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(uri: String) {
        preferencesRepository.setAutoBackupUri(uri)
    }
}