package dev.muffar.moneyfikasi.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.muffar.moneyfikasi.data.db.MoneyfikasiDatabase
import dev.muffar.moneyfikasi.data.db.dao.CategoryDao
import dev.muffar.moneyfikasi.data.db.dao.TransactionDao
import dev.muffar.moneyfikasi.data.db.dao.WalletDao
import dev.muffar.moneyfikasi.data.repositoy.BackupRestoreRepositoryImpl
import dev.muffar.moneyfikasi.data.repositoy.CategoryRepositoryImpl
import dev.muffar.moneyfikasi.data.repositoy.TransactionRepositoryImpl
import dev.muffar.moneyfikasi.data.repositoy.WalletRepositoryImpl
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
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
    fun provideTransactionRepository(transactionDao: TransactionDao): TransactionRepository {
        return TransactionRepositoryImpl(transactionDao)
    }

    @Provides
    @Singleton
    fun provideBackupRestoreRepository(
        @ApplicationContext context: Context,
        db: MoneyfikasiDatabase,
    ): BackupRestoreRepository {
        return BackupRestoreRepositoryImpl(context, db)
    }
}