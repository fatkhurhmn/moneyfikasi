package dev.muffar.moneyfikasi.data.worker

import android.content.Context
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository
import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository
import kotlinx.coroutines.flow.first

@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val backupRestoreRepository: BackupRestoreRepository,
    private val backupSettingsRepository: BackupSettingsRepository,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val settings = backupSettingsRepository.getBackupSettings().first()
        val isEnabled = settings.autoBackup.isEnabled
        if (!isEnabled) return Result.success()

        val uriString = settings.autoBackup.uri
        if (uriString.isEmpty()) return Result.failure()

        val uri = uriString.toUri()
        val isDeletePreviousBackup = settings.isDeletePreviousBackup
        val previousBackup = settings.latestBackup

        return try {
            val result = backupRestoreRepository.backupData(uri)
            if (result.isSuccess) {
                val newFileName = result.getOrThrow()
                if (isDeletePreviousBackup && previousBackup.name.isNotEmpty() && previousBackup.folder.isNotEmpty()) {
                    backupRestoreRepository.deleteBackup(previousBackup)
                }
                val latestBackup = LatestBackup(
                    name = newFileName,
                    date = System.currentTimeMillis(),
                    folder = uri.toString()
                )
                backupSettingsRepository.setLatestBackup(latestBackup)
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