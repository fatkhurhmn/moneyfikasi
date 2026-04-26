package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class SetLatestBackup(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(fileName: String, date: Long, folder: String) {
        preferencesRepository.setLatestBackup(fileName, date, folder)
    }
}
