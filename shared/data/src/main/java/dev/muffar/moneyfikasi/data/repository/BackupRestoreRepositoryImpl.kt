package dev.muffar.moneyfikasi.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dev.muffar.moneyfikasi.data.db.MoneyfikasiDatabase
import dev.muffar.moneyfikasi.data.worker.BackupWorker
import dev.muffar.moneyfikasi.data.worker.DriveBackupWorker
import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.system.exitProcess

class BackupRestoreRepositoryImpl(
    private val context: Context,
    private val db: MoneyfikasiDatabase,
) : BackupRestoreRepository {

    override suspend fun backupData(uri: Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            checkpoint()

            val dbFile = context.getDatabasePath(MoneyfikasiDatabase.DATABASE_NAME)
            val filesToBackup = listOf(
                dbFile,
                File(dbFile.path + MoneyfikasiDatabase.SQLITE_WAL_FILE_SUFFIX),
                File(dbFile.path + MoneyfikasiDatabase.SQLITE_SHM_FILE_SUFFIX)
            ).filter { it.exists() }

            val targetDocumentFile = getTargetDirectory(uri)
                ?: return@withContext Result.failure(IOException("Failed to get target directory"))

            val fileName = getBackupFileName()
            val backupFile = targetDocumentFile.createFile("application/zip", fileName)
                ?: return@withContext Result.failure(IOException("Failed to create backup file"))

            context.contentResolver.openOutputStream(backupFile.uri)?.use { outputStream ->
                ZipOutputStream(outputStream).use { zipOut ->
                    filesToBackup.forEach { file ->
                        zipOut.putNextEntry(ZipEntry(file.name))
                        file.inputStream().use { input ->
                            input.copyTo(zipOut)
                        }
                        zipOut.closeEntry()
                    }
                }
            }
            Result.success(fileName)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun restoreData(uri: Uri, restart: Boolean): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                db.close()

                val dbFile = context.getDatabasePath(MoneyfikasiDatabase.DATABASE_NAME)
                val dbDir = dbFile.parentFile
                    ?: return@withContext Result.failure(IOException("Database directory not found"))

                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    ZipInputStream(inputStream).use { zipIn ->
                        var entry = zipIn.nextEntry
                        while (entry != null) {
                            val outFile = File(dbDir, entry.name)
                            FileOutputStream(outFile).use { output ->
                                zipIn.copyTo(output)
                            }
                            zipIn.closeEntry()
                            entry = zipIn.nextEntry
                        }
                    }
                }

                checkpoint()

                if (restart) {
                    restartApp()
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun deleteBackup(latestBackup: LatestBackup): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val uri = latestBackup.folder.toUri()
                val targetDocumentFile = getTargetDirectory(uri)
                    ?: return@withContext Result.failure(IOException("Failed to get target directory"))

                val backupFile = targetDocumentFile.findFile(latestBackup.name)
                    ?: return@withContext Result.failure(IOException("Backup file not found"))

                if (backupFile.delete()) {
                    Result.success(Unit)
                } else {
                    Result.failure(IOException("Failed to delete backup file"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    private fun getTargetDirectory(uri: Uri): DocumentFile? {
        return when (uri.scheme) {
            "content" -> DocumentFile.fromTreeUri(context, uri)
            "file" -> uri.path?.let { DocumentFile.fromFile(File(it)) }
            else -> null
        }
    }

    override fun takePersistableUriPermission(uri: Uri) {
        if (uri.scheme == "content") {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }
    }

    override fun scheduleAutoBackup(isEnabled: Boolean, uri: String, period: TimePeriod) {
        if (!isEnabled || uri.isEmpty()) return

        val (interval, timeUnit) = backupInterval(period)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(requiresCharging = false)
            .setRequiresBatteryNotLow(requiresBatteryNotLow = false)
            .build()

        val backupRequest = PeriodicWorkRequestBuilder<BackupWorker>(interval, timeUnit)
            .setConstraints(constraints)
            .setInitialDelay(delayUntilMidnight(), TimeUnit.MILLISECONDS)
            .addTag(BACKUP_TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            AUTO_BACKUP_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            backupRequest
        )
    }

    override fun cancelAutoBackup() {
        WorkManager.getInstance(context).cancelUniqueWork(AUTO_BACKUP_WORK_NAME)
    }

    override fun scheduleDriveAutoBackup(isSignedIn: Boolean, period: TimePeriod) {
        if (!isSignedIn) return

        val (interval, timeUnit) = backupInterval(period)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(requiresCharging = false)
            .setRequiresBatteryNotLow(requiresBatteryNotLow = false)
            .build()

        val backupRequest = PeriodicWorkRequestBuilder<DriveBackupWorker>(interval, timeUnit)
            .setConstraints(constraints)
            .setInitialDelay(delayUntilMidnight(), TimeUnit.MILLISECONDS)
            .addTag(DRIVE_BACKUP_TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DRIVE_AUTO_BACKUP_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            backupRequest
        )
    }

    override fun cancelDriveAutoBackup() {
        WorkManager.getInstance(context).cancelUniqueWork(DRIVE_AUTO_BACKUP_WORK_NAME)
    }

    private fun backupInterval(period: TimePeriod): Pair<Long, TimeUnit> {
        return when (period) {
            TimePeriod.DAILY -> 1L to TimeUnit.DAYS
            TimePeriod.WEEKLY -> 7L to TimeUnit.DAYS
            TimePeriod.MONTHLY -> 30L to TimeUnit.DAYS
            else -> 1L to TimeUnit.DAYS
        }
    }

    private fun delayUntilMidnight(): Long {
        val currentTime = LocalDateTime.now()
        val nextMidnight = currentTime.plusDays(1).with(LocalTime.MIDNIGHT)
        return Duration.between(currentTime, nextMidnight).toMillis()
    }

    private fun checkpoint() {
        val supportDb = db.openHelper.writableDatabase
        supportDb.query("PRAGMA wal_checkpoint(FULL);").close()
        supportDb.query("PRAGMA wal_checkpoint(TRUNCATE);").close()
    }

    private fun getBackupFileName(): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "moneyfikasi_$timestamp.zip"
    }

    private fun restartApp() {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        exitProcess(0)
    }

    companion object {
        const val AUTO_BACKUP_WORK_NAME = "auto_backup_work"
        const val DRIVE_AUTO_BACKUP_WORK_NAME = "drive_auto_backup_work"
        private const val BACKUP_TAG = "BACKUP_TAG"
        private const val DRIVE_BACKUP_TAG = "DRIVE_BACKUP_TAG"
    }

}
