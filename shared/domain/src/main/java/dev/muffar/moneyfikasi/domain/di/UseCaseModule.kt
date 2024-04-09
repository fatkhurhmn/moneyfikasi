package dev.muffar.moneyfikasi.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import dev.muffar.moneyfikasi.domain.repository.WalletRepository
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.category.DeleteAllCategories
import dev.muffar.moneyfikasi.domain.usecase.category.DeleteCategory
import dev.muffar.moneyfikasi.domain.usecase.category.GetAllCategories
import dev.muffar.moneyfikasi.domain.usecase.category.GetCategoryById
import dev.muffar.moneyfikasi.domain.usecase.category.SaveAllCategories
import dev.muffar.moneyfikasi.domain.usecase.category.SaveCategory
import dev.muffar.moneyfikasi.domain.usecase.category.UpdateCategory
import dev.muffar.moneyfikasi.domain.usecase.wallet.DeleteAllWallets
import dev.muffar.moneyfikasi.domain.usecase.wallet.DeleteWallet
import dev.muffar.moneyfikasi.domain.usecase.wallet.GetAllWallets
import dev.muffar.moneyfikasi.domain.usecase.wallet.GetWalletById
import dev.muffar.moneyfikasi.domain.usecase.wallet.SaveAllWallets
import dev.muffar.moneyfikasi.domain.usecase.wallet.SaveWallet
import dev.muffar.moneyfikasi.domain.usecase.wallet.UpdateWallet
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideCategoryUseCases(
        categoryRepository: CategoryRepository,
    ) = CategoryUseCases(
        saveCategory = SaveCategory(categoryRepository),
        saveAllCategories = SaveAllCategories(categoryRepository),
        updateCategory = UpdateCategory(categoryRepository),
        deleteCategory = DeleteCategory(categoryRepository),
        deleteAllCategories = DeleteAllCategories(categoryRepository),
        getAllCategories = GetAllCategories(categoryRepository),
        getCategoryById = GetCategoryById(categoryRepository),
    )

    @Provides
    fun provideWalletUseCases(
        walletRepository: WalletRepository,
    ) = WalletUseCases(
        getAllWallets = GetAllWallets(walletRepository),
        getWalletById = GetWalletById(walletRepository),
        saveWallet = SaveWallet(walletRepository),
        saveAllWallets = SaveAllWallets(walletRepository),
        updateWallet = UpdateWallet(walletRepository),
        deleteWallet = DeleteWallet(walletRepository),
        deleteAllWallets = DeleteAllWallets(walletRepository),
    )
}