package dev.muffar.moneyfikasi.data.utils

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.muffar.moneyfikasi.data.db.MoneyfikasiDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PrepopulateDbCallback(private val context: Context) : RoomDatabase.Callback() {
    private val scope = CoroutineScope(SupervisorJob())
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val moneyfikasiDb = MoneyfikasiDatabase.create(context)
        val categoryDao = moneyfikasiDb.categoryDao()
        val walletDao = moneyfikasiDb.walletDao()
        scope.launch {
            categoryDao.saveAll(InitDataSource.getCategories())
            walletDao.saveAll(InitDataSource.getWallets())
        }
    }
}