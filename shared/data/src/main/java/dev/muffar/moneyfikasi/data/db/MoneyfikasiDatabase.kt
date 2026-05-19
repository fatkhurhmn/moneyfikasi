package dev.muffar.moneyfikasi.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.muffar.moneyfikasi.data.db.dao.BudgetDao
import dev.muffar.moneyfikasi.data.db.dao.CategoryDao
import dev.muffar.moneyfikasi.data.db.dao.PresetDao
import dev.muffar.moneyfikasi.data.db.dao.RecurringTransactionDao
import dev.muffar.moneyfikasi.data.db.dao.TransactionDao
import dev.muffar.moneyfikasi.data.db.dao.WalletDao
import dev.muffar.moneyfikasi.data.db.entity.BudgetEntity
import dev.muffar.moneyfikasi.data.db.entity.CategoryEntity
import dev.muffar.moneyfikasi.data.db.entity.PresetEntity
import dev.muffar.moneyfikasi.data.db.entity.RecurringTransactionEntity
import dev.muffar.moneyfikasi.data.db.entity.TransactionEntity
import dev.muffar.moneyfikasi.data.db.entity.WalletEntity
import dev.muffar.moneyfikasi.data.utils.PrepopulateDbCallback

@Database(
    version = 3,
    entities = [
        CategoryEntity::class,
        TransactionEntity::class,
        WalletEntity::class,
        PresetEntity::class,
        BudgetEntity::class,
        RecurringTransactionEntity::class,
    ],
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MoneyfikasiDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun walletDao(): WalletDao
    abstract fun presetDao(): PresetDao
    abstract fun budgetDao(): BudgetDao
    abstract fun recurringTransactionDao(): RecurringTransactionDao

    companion object {
        const val DATABASE_NAME = "moneyfikasi"
        const val SQLITE_WAL_FILE_SUFFIX = "-wal"
        const val SQLITE_SHM_FILE_SUFFIX = "-shm"

        @Volatile
        private var INSTANCE: MoneyfikasiDatabase? = null

        fun create(context: Context): MoneyfikasiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoneyfikasiDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(PrepopulateDbCallback(context))
                    .fallbackToDestructiveMigration(true)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}