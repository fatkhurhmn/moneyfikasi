package dev.muffar.moneyfikasi.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository
import dev.muffar.moneyfikasi.domain.repository.DriveBackupRepository
import kotlinx.coroutines.flow.first

@HiltWorker
class DriveBackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val driveBackupRepository: DriveBackupRepository,
    private val backupSettingsRepository: BackupSettingsRepository,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val settings = backupSettingsRepository.getBackupSettings().first()
        if (!settings.isDriveAutoBackupEnabled) return Result.success()

        val signedIn = try {
            driveBackupRepository.isSignedIn()
        } catch (_: Exception) {
            false
        }
        if (!signedIn) return Result.success()

        return try {
            val result = driveBackupRepository.backupToDrive()
            if (result.isSuccess) Result.success() else Result.retry()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
