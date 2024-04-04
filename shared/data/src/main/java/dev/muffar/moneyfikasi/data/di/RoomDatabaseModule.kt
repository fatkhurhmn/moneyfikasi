package dev.muffar.moneyfikasi.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.muffar.moneyfikasi.data.db.MoneyfikasiDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    @Provides
    @Singleton
    fun provideMoneyfikasiDatabase(
        @ApplicationContext context: Context,
    ): MoneyfikasiDatabase {
        return MoneyfikasiDatabase.create(context)
    }

    @Provides
    @Singleton
    fun provideTransactionDao(db: MoneyfikasiDatabase) = db.transactionDao()

    @Provides
    @Singleton
    fun provideCategoryDao(db: MoneyfikasiDatabase) = db.categoryDao()

    @Provides
    @Singleton
    fun provideWalletDao(db: MoneyfikasiDatabase) = db.walletDao()

    @Provides
    @Singleton
    fun provideLoanDao(db: MoneyfikasiDatabase) = db.loanDao()
}