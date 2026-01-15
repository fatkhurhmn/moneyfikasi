package dev.muffar.moneyfikasi.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import dev.muffar.moneyfikasi.domain.repository.WalletRepository
import dev.muffar.moneyfikasi.domain.usecase.backup_restore.BackupData
import dev.muffar.moneyfikasi.domain.usecase.backup_restore.BackupRestoreUseCases
import dev.muffar.moneyfikasi.domain.usecase.backup_restore.RestoreData
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.category.DeleteCategory
import dev.muffar.moneyfikasi.domain.usecase.category.GetAllCategories
import dev.muffar.moneyfikasi.domain.usecase.category.GetCategoryById
import dev.muffar.moneyfikasi.domain.usecase.category.UpsertCategory
import dev.muffar.moneyfikasi.domain.usecase.preferences.IsBalanceVisible
import dev.muffar.moneyfikasi.domain.usecase.preferences.PreferencesUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.SetBalanceVisibility
import dev.muffar.moneyfikasi.domain.usecase.transaction.DeleteTransaction
import dev.muffar.moneyfikasi.domain.usecase.transaction.GetAllTransactions
import dev.muffar.moneyfikasi.domain.usecase.transaction.GetTransactionById
import dev.muffar.moneyfikasi.domain.usecase.transaction.GetTransactions
import dev.muffar.moneyfikasi.domain.usecase.transaction.AddTransaction
import dev.muffar.moneyfikasi.domain.usecase.transaction.SaveTransactionImage
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.UpdateTransaction
import dev.muffar.moneyfikasi.domain.usecase.wallet.DeleteWallet
import dev.muffar.moneyfikasi.domain.usecase.wallet.GetAllWallets
import dev.muffar.moneyfikasi.domain.usecase.wallet.GetWalletById
import dev.muffar.moneyfikasi.domain.usecase.wallet.UpsertWallet
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideCategoryUseCases(
        categoryRepository: CategoryRepository,
    ) = CategoryUseCases(
        upsertCategory = UpsertCategory(categoryRepository),
        deleteCategory = DeleteCategory(categoryRepository),
        getAllCategories = GetAllCategories(categoryRepository),
        getCategoryById = GetCategoryById(categoryRepository),
    )

    @Provides
    fun provideWalletUseCases(
        walletRepository: WalletRepository,
    ) = WalletUseCases(
        upsertWallet = UpsertWallet(walletRepository),
        deleteWallet = DeleteWallet(walletRepository),
        getAllWallets = GetAllWallets(walletRepository),
        getWalletById = GetWalletById(walletRepository),
    )

    @Provides
    fun provideTransactionUseCases(
        transactionRepository: TransactionRepository,
    ) = TransactionUseCases(
        addTransaction = AddTransaction(transactionRepository),
        updateTransaction = UpdateTransaction(transactionRepository),
        deleteTransaction = DeleteTransaction(transactionRepository),
        getTransactionById = GetTransactionById(transactionRepository),
        getAllTransactions = GetAllTransactions(transactionRepository),
        getTransactions = GetTransactions(transactionRepository),
        saveTransactionImage = SaveTransactionImage()
    )

    @Provides
    fun provideBackupRestoreUseCases(
        backupRestoreRepository: BackupRestoreRepository,
    ) = BackupRestoreUseCases(
        backupData = BackupData(backupRestoreRepository),
        restoreData = RestoreData(backupRestoreRepository),
    )

    @Provides
    fun providePreferencesUseCases(
        preferencesRepository: PreferencesRepository,
    ) = PreferencesUseCases(
        setBalanceVisibility = SetBalanceVisibility(preferencesRepository),
        isBalanceVisible = IsBalanceVisible(preferencesRepository),
    )
}