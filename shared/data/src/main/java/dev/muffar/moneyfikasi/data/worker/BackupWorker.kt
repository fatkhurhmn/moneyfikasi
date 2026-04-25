package dev.muffar.moneyfikasi.data.worker

import android.content.Context
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository
import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.first

@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val backupRestoreRepository: BackupRestoreRepository,
    private val preferencesRepository: PreferencesRepository,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val isEnabled = preferencesRepository.isAutoBackupEnabled().first()
        if (!isEnabled) return Result.success()

        val uriString = preferencesRepository.getAutoBackupUri().first()
        if (uriString.isEmpty()) return Result.failure()

        val uri = uriString.toUri()
        
        return try {
            val result = backupRestoreRepository.backupData(uri)
            if (result.isSuccess) {
                preferencesRepository.setLatestBackup(result.getOrThrow(), System.currentTimeMillis())
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}