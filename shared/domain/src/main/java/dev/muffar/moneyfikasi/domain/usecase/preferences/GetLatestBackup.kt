package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetLatestBackup(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<LatestBackup> {
        return combine(
            preferencesRepository.getLatestBackupName(),
            preferencesRepository.getLatestBackupDate(),
            preferencesRepository.getLatestBackupFolder()
        ) { name, date, folder ->
            LatestBackup(name, date, folder)
        }
    }
}
