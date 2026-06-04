package dev.muffar.moneyfikasi

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import dev.muffar.moneyfikasi.data.utils.RecurringTransactionScheduler
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.UiSettingsUseCases
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class MoneyfikasiApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var uiSettingsUseCases: UiSettingsUseCases

    override fun onCreate() {
        super.onCreate()
        applyAppTheme()
        AndroidThreeTen.init(this)
        RecurringTransactionScheduler(this).scheduleRecurringTransaction()
    }

    private fun applyAppTheme() {
        val uiSettings = runBlocking {
            uiSettingsUseCases.getUiSettings().first()
        }
        AppCompatDelegate.setDefaultNightMode(uiSettings.appTheme.toAppCompatNightMode())
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
