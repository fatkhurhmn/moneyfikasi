package dev.muffar.moneyfikasi.data.repositoy

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.muffar.moneyfikasi.data.db.MoneyfikasiDatabase
import dev.muffar.moneyfikasi.data.remote.drive.DriveAuthHelper
import dev.muffar.moneyfikasi.data.remote.drive.DriveBackupDataSource
import dev.muffar.moneyfikasi.domain.model.DriveBackupFile
import dev.muffar.moneyfikasi.domain.repository.DriveBackupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.inject.Inject
import kotlin.system.exitProcess

class DriveBackupRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val db: MoneyfikasiDatabase,
    private val authHelper: DriveAuthHelper,
    private val dataSource: DriveBackupDataSource
) : DriveBackupRepository {

    override suspend fun isSignedIn(): Boolean =
        withContext(Dispatchers.IO) { authHelper.isSignedIn() }

    override suspend fun signOut() {
        authHelper.signOut()
    }

    override suspend fun getDriveBackups(): Result<List<DriveBackupFile>> =
        withContext(Dispatchers.IO) {
            try {
                Result.success(dataSource.listBackups())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun uploadFile(file: File, fileName: String): Result<DriveBackupFile> =
        withContext(Dispatchers.IO) {
            try {
                Result.success(dataSource.upload(file, fileName))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun backupToDrive(): Result<DriveBackupFile> =
        withContext(Dispatchers.IO) {
            try {
                if (!authHelper.isSignedIn()) {
                    return@withContext Result.failure(
                        IllegalStateException("Not signed in to Google Drive")
                    )
                }
                checkpoint()
                val dbFile = context.getDatabasePath(MoneyfikasiDatabase.DATABASE_NAME)
                val filesToBackup = listOf(
                    dbFile,
                    File(dbFile.path + MoneyfikasiDatabase.SQLITE_WAL_FILE_SUFFIX),
                    File(dbFile.path + MoneyfikasiDatabase.SQLITE_SHM_FILE_SUFFIX)
                ).filter { it.exists() }

                if (filesToBackup.isEmpty()) {
                    return@withContext Result.failure(
                        IllegalStateException("Database file not found")
                    )
                }

                val fileName = getBackupFileName()
                val tmpFile = File.createTempFile("moneyfikasi_backup", ".zip", context.cacheDir)
                try {
                    ZipOutputStream(FileOutputStream(tmpFile)).use { zipOut ->
                        filesToBackup.forEach { file ->
                            zipOut.putNextEntry(ZipEntry(file.name))
                            file.inputStream().use { input -> input.copyTo(zipOut) }
                            zipOut.closeEntry()
                        }
                    }
                    val uploaded = dataSource.upload(tmpFile, fileName)
                    // Single online backup: remove older files, keep only the latest upload.
                    try {
                        dataSource.listBackups()
                            .filter { it.id != uploaded.id }
                            .forEach { runCatching { dataSource.delete(it.id) } }
                    } catch (_: Exception) {
                    }
                    Result.success(uploaded)
                } finally {
                    tmpFile.delete()
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun restoreFromDrive(fileId: String, restart: Boolean): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val tmpFile = File.createTempFile("moneyfikasi_restore", ".zip", context.cacheDir)
                try {
                    dataSource.downloadTo(fileId, tmpFile)
                    db.close()

                    val dbFile = context.getDatabasePath(MoneyfikasiDatabase.DATABASE_NAME)
                    val dbDir = dbFile.parentFile
                        ?: return@withContext Result.failure(
                            IllegalStateException("Database directory not found")
                        )

                    tmpFile.inputStream().use { inputStream ->
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
                    if (restart) restartApp()
                    Result.success(Unit)
                } finally {
                    tmpFile.delete()
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun deleteDriveBackup(fileId: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                dataSource.delete(fileId)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
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
}
