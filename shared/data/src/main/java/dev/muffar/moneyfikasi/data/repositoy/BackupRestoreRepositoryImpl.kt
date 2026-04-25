package dev.muffar.moneyfikasi.data.repositoy

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import dev.muffar.moneyfikasi.data.db.MoneyfikasiDatabase
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

            val targetDocumentFile = DocumentFile.fromTreeUri(context, uri)
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
                val dbDir = dbFile.parentFile ?: return@withContext Result.failure(IOException("Database directory not found"))

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
