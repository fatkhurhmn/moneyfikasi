package dev.muffar.moneyfikasi.data.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.muffar.moneyfikasi.data.db.MoneyfikasiDatabase
import dev.muffar.moneyfikasi.data.remote.drive.DriveAuthHelper
import dev.muffar.moneyfikasi.data.remote.drive.DriveBackupDataSource
import dev.muffar.moneyfikasi.data.repositoy.DriveBackupRepositoryImpl
import dev.muffar.moneyfikasi.domain.repository.DriveBackupRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DriveModule {

    @Provides
    @Singleton
    fun provideGoogleSignInClient(
        @ApplicationContext context: Context
    ): GoogleSignInClient {
        return GoogleSignIn.getClient(context, DriveAuthHelper.buildSignInOptions())
    }

    @Provides
    @Singleton
    fun provideDriveBackupRepository(
        @ApplicationContext context: Context,
        db: MoneyfikasiDatabase,
        authHelper: DriveAuthHelper,
        dataSource: DriveBackupDataSource
    ): DriveBackupRepository {
        return DriveBackupRepositoryImpl(context, db, authHelper, dataSource)
    }
}
