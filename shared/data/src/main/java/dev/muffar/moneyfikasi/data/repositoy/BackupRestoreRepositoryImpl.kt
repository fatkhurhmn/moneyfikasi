package dev.muffar.moneyfikasi.data.repositoy

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import dev.muffar.moneyfikasi.data.db.MoneyfikasiDatabase
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository
import java.io.File
import java.io.IOException
import kotlin.system.exitProcess

class BackupRestoreRepositoryImpl(
    private val context: Context,
    private val db: MoneyfikasiDatabase,
) : BackupRestoreRepository {
    override fun backupData(uri: Uri): Int {
        var result = -99
        val dbFile = context.getDatabasePath(MoneyfikasiDatabase.DATABASE_NAME)
        val dbWalFile = File(dbFile.path + MoneyfikasiDatabase.SQLITE_WAL_FILE_SUFFIX)
        val dbShmFile = File(dbFile.path + MoneyfikasiDatabase.SQLITE_SHM_FILE_SUFFIX)
        checkpoint()

        try {
            val targetDocumentFile = DocumentFile.fromTreeUri(context, uri)
            targetDocumentFile?.findFile(BACKUP_NAME)?.delete()
            targetDocumentFile?.findFile(BACKUP_WAL_NAME)?.delete()
            targetDocumentFile?.findFile(BACKUP_SHM_NAME)?.delete()

            val backupFile =
                targetDocumentFile?.createFile(
                    "application/octet-stream",
                    BACKUP_NAME
                )
            val backupWalFile = targetDocumentFile?.createFile(
                "application/octet-stream",
                BACKUP_WAL_NAME
            )
            val backupShmFile = targetDocumentFile?.createFile(
                "application/octet-stream",
                BACKUP_SHM_NAME
            )

            context.contentResolver.openOutputStream(backupFile!!.uri).use { output ->
                dbFile.inputStream().use { input -> input.copyTo(output!!) }
            }

            if (dbWalFile.exists()) {
                context.contentResolver.openOutputStream(backupWalFile!!.uri).use { output ->
                    dbWalFile.inputStream().use { input -> input.copyTo(output!!) }
                }
            }

            if (dbShmFile.exists()) {
                context.contentResolver.openOutputStream(backupShmFile!!.uri).use { output ->
                    dbShmFile.inputStream().use { input -> input.copyTo(output!!) }
                }
            }

            result = 0
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    override fun restoreData(uri: Uri, restart: Boolean) {
        val contentResolver = context.contentResolver

        val dbFile = context.getDatabasePath(MoneyfikasiDatabase.DATABASE_NAME)
        val dbWalFile = File(dbFile.path + MoneyfikasiDatabase.SQLITE_WAL_FILE_SUFFIX)
        val dbShmFile = File(dbFile.path + MoneyfikasiDatabase.SQLITE_SHM_FILE_SUFFIX)

        try {
            val bkpFileUri =
                DocumentFile.fromTreeUri(context, uri)?.findFile(BACKUP_NAME)?.uri ?: return
            val inputStream = contentResolver.openInputStream(bkpFileUri)
                ?: throw IOException("Failed to get input stream")
            dbFile.outputStream().use { outputStream -> inputStream.copyTo(outputStream) }

            val bkpWalFileUri =
                DocumentFile.fromTreeUri(context, uri)?.findFile(BACKUP_WAL_NAME)?.uri
            if (bkpWalFileUri != null && dbWalFile.exists()) {
                val walInputStream = contentResolver.openInputStream(bkpWalFileUri)
                    ?: throw IOException("Failed to get input stream")
                dbWalFile.outputStream().use { outputStream -> walInputStream.copyTo(outputStream) }
            }

            val bkpShmFileUri =
                DocumentFile.fromTreeUri(context, uri)?.findFile(BACKUP_SHM_NAME)?.uri
            if (bkpShmFileUri != null && dbShmFile.exists()) {
                val shmInputStream = contentResolver.openInputStream(bkpShmFileUri)
                    ?: throw IOException("Failed to get input stream")
                dbShmFile.outputStream().use { outputStream -> shmInputStream.copyTo(outputStream) }
            }

            checkpoint()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (restart) {
            val i = context.packageManager.getLaunchIntentForPackage(context.packageName)
            i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(i)
            exitProcess(0)
        }
    }

    private fun checkpoint() {
        val db = db.openHelper.writableDatabase
        db.query("PRAGMA wal_checkpoint(FULL);")
        db.query("PRAGMA wal_checkpoint(TRUNCATE);")
    }

    companion object{
        private const val BACKUP_NAME = "moneyfikasi_backup"
        private const val BACKUP_WAL_NAME = MoneyfikasiDatabase.DATABASE_BACKUP_NAME + "-wal"
        private const val BACKUP_SHM_NAME = MoneyfikasiDatabase.DATABASE_BACKUP_NAME + "-shm"
    }
}