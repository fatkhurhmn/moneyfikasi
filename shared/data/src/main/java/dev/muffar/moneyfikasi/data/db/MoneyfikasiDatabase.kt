package dev.muffar.moneyfikasi.data.db

import android.content.Context
import android.content.Intent
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.muffar.moneyfikasi.data.db.dao.CategoryDao
import dev.muffar.moneyfikasi.data.db.dao.LoanDao
import dev.muffar.moneyfikasi.data.db.dao.TransactionDao
import dev.muffar.moneyfikasi.data.db.dao.WalletDao
import dev.muffar.moneyfikasi.data.db.entity.CategoryEntity
import dev.muffar.moneyfikasi.data.db.entity.LoanEntity
import dev.muffar.moneyfikasi.data.db.entity.TransactionEntity
import dev.muffar.moneyfikasi.data.db.entity.WalletEntity
import java.io.File
import java.io.IOException
import kotlin.system.exitProcess

@Database(
    version = 2,
    entities = [
        CategoryEntity::class,
        TransactionEntity::class,
        WalletEntity::class,
        LoanEntity::class
    ],
    exportSchema = true
)
@TypeConverters(RoomTypeConverters::class)
abstract class MoneyfikasiDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun walletDao(): WalletDao
    abstract fun loanDao(): LoanDao

    companion object {
        const val DATABASE_NAME = "moneyfikasi"
        const val SQLITE_WAL_FILE_SUFFIX = "-wal"
        const val SQLITE_SHM_FILE_SUFFIX = "-shm"
        const val DATABASE_BACKUP_SUFFIX = "-bkp"

        fun create(applicationContext: Context): MoneyfikasiDatabase {
            return Room
                .databaseBuilder(
                    applicationContext,
                    MoneyfikasiDatabase::class.java,
                    DATABASE_NAME
                )
                .createFromAsset("moneyfikasi.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    fun backupDatabase(context: Context): Int {
        var result = -99
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        val dbWalFile = File(dbFile.path + SQLITE_WAL_FILE_SUFFIX)
        val dbShmFile = File(dbFile.path + SQLITE_SHM_FILE_SUFFIX)
        val backupFile = File(dbFile.path + DATABASE_BACKUP_SUFFIX)
        val backupWalFile = File(backupFile.path + SQLITE_WAL_FILE_SUFFIX)
        val backupShmFile = File(backupFile.path + SQLITE_SHM_FILE_SUFFIX)

        if (backupFile.exists()) backupFile.delete()
        if (backupWalFile.exists()) backupWalFile.delete()
        if (backupShmFile.exists()) backupShmFile.delete()
        checkpoint()

        try {
            dbFile.copyTo(backupFile, true)
            if (dbWalFile.exists()) dbWalFile.copyTo(backupWalFile, true)
            if (dbShmFile.exists()) dbShmFile.copyTo(backupShmFile, true)
            result = 0
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    fun restoreDatabase(context: Context, restart: Boolean = true) {
        if (!File(context.getDatabasePath(DATABASE_NAME).path + DATABASE_BACKUP_SUFFIX).exists()) return
        val dbPath = this.openHelper.readableDatabase.path
        val dbFile = dbPath?.let { File(it) }
        if (dbFile == null) return
        val dbWalFile = File(dbFile.path + SQLITE_WAL_FILE_SUFFIX)
        val dbShmFile = File(dbFile.path + SQLITE_SHM_FILE_SUFFIX)
        val backupFile = File(context.getDatabasePath(DATABASE_NAME).path + DATABASE_BACKUP_SUFFIX)
        val backupWalFile = File(backupFile.path + SQLITE_WAL_FILE_SUFFIX)
        val backupShmFile = File(backupFile.path + SQLITE_SHM_FILE_SUFFIX)

        try {
            backupFile.copyTo(dbFile, true)
            if (backupWalFile.exists()) backupWalFile.copyTo(dbWalFile, true)
            if (backupShmFile.exists()) backupShmFile.copyTo(dbShmFile, true)
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
        val db = this.openHelper.writableDatabase
        db.query("PRAGMA wal_checkpoint(FULL);")
        db.query("PRAGMA wal_checkpoint(TRUNCATE);")
    }
}