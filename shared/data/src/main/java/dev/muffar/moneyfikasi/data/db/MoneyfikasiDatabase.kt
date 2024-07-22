package dev.muffar.moneyfikasi.data.db

import android.content.Context
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
        const val DATABASE_BACKUP_NAME = "moneyfikasi_backup"
        const val SQLITE_WAL_FILE_SUFFIX = "-wal"
        const val SQLITE_SHM_FILE_SUFFIX = "-shm"

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
}