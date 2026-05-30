package dev.muffar.moneyfikasi.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.muffar.moneyfikasi.data.preferences.serializer.BackupSettingsSerializer
import dev.muffar.moneyfikasi.data.preferences.serializer.NotificationSettingsSerializer
import dev.muffar.moneyfikasi.data.preferences.serializer.SecuritySettingsSerializer
import dev.muffar.moneyfikasi.data.preferences.serializer.UiSettingsSerializer
import dev.muffar.moneyfikasi.domain.model.BackupSettings
import dev.muffar.moneyfikasi.domain.model.NotificationSettings
import dev.muffar.moneyfikasi.domain.model.SecuritySettings
import dev.muffar.moneyfikasi.domain.model.UiSettings
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUiDataStore(
        @ApplicationContext context: Context,
    ): DataStore<UiSettings> {
        return DataStoreFactory.create(
            serializer = UiSettingsSerializer,
            produceFile = {
                context.dataStoreFile("ui_settings.json")
            },
            corruptionHandler = ReplaceFileCorruptionHandler { UiSettings() }
        )
    }

    @Provides
    @Singleton
    fun provideBackupDataStore(
        @ApplicationContext context: Context,
    ): DataStore<BackupSettings> {
        return DataStoreFactory.create(
            serializer = BackupSettingsSerializer,
            produceFile = {
                context.dataStoreFile("backup_settings.json")
            },
            corruptionHandler = ReplaceFileCorruptionHandler { BackupSettings() }
        )
    }

    @Provides
    @Singleton
    fun provideNotificationDataStore(
        @ApplicationContext context: Context,
    ): DataStore<NotificationSettings> {
        return DataStoreFactory.create(
            serializer = NotificationSettingsSerializer,
            produceFile = {
                context.dataStoreFile("notification_settings.json")
            },
            corruptionHandler = ReplaceFileCorruptionHandler { NotificationSettings() }
        )
    }

    @Provides
    @Singleton
    fun provideSecurityDataStore(
        @ApplicationContext context: Context,
    ): DataStore<SecuritySettings> {
        return DataStoreFactory.create(
            serializer = SecuritySettingsSerializer,
            produceFile = {
                context.dataStoreFile("security_settings.json")
            },
            corruptionHandler = ReplaceFileCorruptionHandler { SecuritySettings() }
        )
    }
}
