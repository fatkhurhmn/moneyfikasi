package dev.muffar.moneyfikasi.data.worker

import android.content.Context
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository
import kotlinx.coroutines.flow.first

@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val backupRestoreRepository: BackupRestoreRepository,
    private val backupSettingsRepository: BackupSettingsRepository,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val isEnabled = backupSettingsRepository.isAutoBackupEnabled().first()
        if (!isEnabled) return Result.success()

        val uriString = backupSettingsRepository.getAutoBackupUri().first()
        if (uriString.isEmpty()) return Result.failure()

        val uri = uriString.toUri()
        val isDeletePreviousBackup = backupSettingsRepository.isDeletePreviousBackup().first()
        val previousBackupName = backupSettingsRepository.getLatestBackupName().first()
        val previousBackupDate = backupSettingsRepository.getLatestBackupDate().first()
        val previousBackupFolder = backupSettingsRepository.getLatestBackupFolder().first()

        return try {
            val result = backupRestoreRepository.backupData(uri)
            if (result.isSuccess) {
                val newFileName = result.getOrThrow()
                if (isDeletePreviousBackup && previousBackupName.isNotEmpty() && previousBackupFolder.isNotEmpty()) {
                    val previousBackup = LatestBackup(
                        name = previousBackupName,
                        date = previousBackupDate,
                        folder = previousBackupFolder
                    )
                    backupRestoreRepository.deleteBackup(previousBackup)
                }
                backupSettingsRepository.setLatestBackup(newFileName, System.currentTimeMillis(), uri.toString())
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}