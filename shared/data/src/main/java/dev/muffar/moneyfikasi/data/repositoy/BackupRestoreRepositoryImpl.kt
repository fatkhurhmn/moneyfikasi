package dev.muffar.moneyfikasi.data.repositoy

import android.content.Context
import dev.muffar.moneyfikasi.data.db.MoneyfikasiDatabase
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository

class BackupRestoreRepositoryImpl(
    private val context: Context,
    private val db: MoneyfikasiDatabase,
) : BackupRestoreRepository {
    override fun backupData(): Int {
        return db.backupDatabase(context)
    }

    override fun restoreData() {
        db.restoreDatabase(context)
    }
}