package dev.muffar.moneyfikasi.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.muffar.moneyfikasi.data.db.MoneyfikasiDatabase
import dev.muffar.moneyfikasi.data.db.dao.BudgetDao
import dev.muffar.moneyfikasi.data.db.dao.CategoryDao
import dev.muffar.moneyfikasi.data.db.dao.PresetDao
import dev.muffar.moneyfikasi.data.db.dao.TransactionDao
import dev.muffar.moneyfikasi.data.db.dao.WalletDao
import dev.muffar.moneyfikasi.data.preferences.BackupPreferences
import dev.muffar.moneyfikasi.data.preferences.SecurityPreferences
import dev.muffar.moneyfikasi.data.preferences.UiPreferences
import dev.muffar.moneyfikasi.data.repositoy.BackupSettingsRepositoryImpl
import dev.muffar.moneyfikasi.data.repositoy.BackupRestoreRepositoryImpl
import dev.muffar.moneyfikasi.data.repositoy.BudgetRepositoryImpl
import dev.muffar.moneyfikasi.data.repositoy.CategoryRepositoryImpl
import dev.muffar.moneyfikasi.data.repositoy.PresetRepositoryImpl
import dev.muffar.moneyfikasi.data.repositoy.SecuritySettingsRepositoryImpl
import dev.muffar.moneyfikasi.data.repositoy.TransactionRepositoryImpl
import dev.muffar.moneyfikasi.data.repositoy.UiSettingsRepositoryImpl
import dev.muffar.moneyfikasi.data.repositoy.WalletRepositoryImpl
import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository
import dev.muffar.moneyfikasi.domain.repository.BudgetRepository
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import dev.muffar.moneyfikasi.domain.repository.PresetRepository
import dev.muffar.moneyfikasi.domain.repository.SecuritySettingsRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import dev.muffar.moneyfikasi.domain.repository.UiSettingsRepository
import dev.muffar.moneyfikasi.domain.repository.WalletRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepositoryImpl(categoryDao)
    }

    @Provides
    @Singleton
    fun provideWalletRepository(walletDao: WalletDao): WalletRepository {
        return WalletRepositoryImpl(walletDao)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(
        transactionDao: TransactionDao,
        walletDao: WalletDao
    ): TransactionRepository {
        return TransactionRepositoryImpl(transactionDao, walletDao)
    }

    @Provides
    @Singleton
    fun provideBackupRestoreRepository(
        @ApplicationContext context: Context,
        db: MoneyfikasiDatabase,
    ): BackupRestoreRepository {
        return BackupRestoreRepositoryImpl(context, db)
    }

    @Provides
    @Singleton
    fun provideUiSettingsRepository(
        uiPreferences: UiPreferences,
    ): UiSettingsRepository {
        return UiSettingsRepositoryImpl(uiPreferences)
    }

    @Provides
    @Singleton
    fun provideBackupSettingsRepository(
        backupPreferences: BackupPreferences,
    ): BackupSettingsRepository {
        return BackupSettingsRepositoryImpl(backupPreferences)
    }

    @Provides
    @Singleton
    fun provideSecuritySettingsRepository(
        securityPreferences: SecurityPreferences,
    ): SecuritySettingsRepository {
        return SecuritySettingsRepositoryImpl(securityPreferences)
    }

    @Provides
    @Singleton
    fun providePresetRepository(presetDao: PresetDao): PresetRepository {
        return PresetRepositoryImpl(presetDao)
    }

    @Provides
    @Singleton
    fun provideBudgetRepository(budgetDao: BudgetDao): BudgetRepository {
        return BudgetRepositoryImpl(budgetDao)
    }
}
