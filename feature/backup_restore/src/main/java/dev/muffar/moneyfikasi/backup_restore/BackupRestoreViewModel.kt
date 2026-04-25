package dev.muffar.moneyfikasi.backup_restore

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.data.worker.BackupWorker
import dev.muffar.moneyfikasi.domain.usecase.backup_restore.BackupRestoreUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.PreferencesUseCases
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BackupRestoreViewModel @Inject constructor(
    private val backupRestoreUseCases: BackupRestoreUseCases,
    private val preferencesUseCases: PreferencesUseCases,
    @param:ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state = mutableStateOf(BackupRestoreState())
    val state: State<BackupRestoreState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        combine(
            preferencesUseCases.getLatestBackup(),
            preferencesUseCases.isAutoBackupEnabled(),
            preferencesUseCases.getAutoBackupUri(),
            preferencesUseCases.getAutoBackupPeriod()
        ) { latestBackup, isAutoBackupEnabled, autoBackupUri, autoBackupPeriod ->
            _state.value = _state.value.copy(
                latestBackupName = latestBackup.name,
                latestBackupDate = latestBackup.date,
                isAutoBackupEnabled = isAutoBackupEnabled,
                autoBackupUri = autoBackupUri,
                autoBackupPeriod = autoBackupPeriod
            )
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: BackupRestoreEvent) {
        when (event) {
            is BackupRestoreEvent.OnBackupData -> backupData(event.uri)
            is BackupRestoreEvent.OnRestoreData -> restoreData(event.uri)
            is BackupRestoreEvent.OnAutoBackupEnabledChanged -> setAutoBackupEnabled(event.isEnabled)
            is BackupRestoreEvent.OnAutoBackupUriChanged -> setAutoBackupUri(event.uri)
            is BackupRestoreEvent.OnAutoBackupPeriodChanged -> setAutoBackupPeriod(event.period)
        }
    }

    private fun backupData(uri: Uri) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            delay(1000)
            backupRestoreUseCases.backupData(uri)
                .onSuccess {
                    preferencesUseCases.setLatestBackup(it, System.currentTimeMillis())
                    _eventFlow.emit(UiEvent.ShowMessage("Backup success", SnackbarType.SUCCESS))
                }
                .onFailure {
                    _eventFlow.emit(
                        UiEvent.ShowMessage(
                            "Backup failed: ${it.message}",
                            SnackbarType.ERROR
                        )
                    )
                }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun restoreData(uri: Uri) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            delay(1000)
            backupRestoreUseCases.restoreData(uri)
                .onSuccess {
                    _eventFlow.emit(UiEvent.ShowMessage("Restore success", SnackbarType.SUCCESS))
                }
                .onFailure {
                    _eventFlow.emit(
                        UiEvent.ShowMessage(
                            "Restore failed: ${it.message}",
                            SnackbarType.ERROR
                        )
                    )
                }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun setAutoBackupEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            preferencesUseCases.setAutoBackupEnabled(isEnabled)
            if (isEnabled) {
                scheduleBackup(isEnabled = true)
            } else {
                cancelBackup()
            }
        }
    }

    private fun setAutoBackupUri(uri: Uri) {
        viewModelScope.launch {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            preferencesUseCases.setAutoBackupUri(uri.toString())
            if (_state.value.isAutoBackupEnabled) {
                scheduleBackup(uri = uri.toString())
            }
        }
    }

    private fun setAutoBackupPeriod(period: String) {
        viewModelScope.launch {
            preferencesUseCases.setAutoBackupPeriod(period)
            if (_state.value.isAutoBackupEnabled) {
                scheduleBackup(period = period)
            }
        }
    }

    private fun scheduleBackup(
        isEnabled: Boolean = _state.value.isAutoBackupEnabled,
        uri: String = _state.value.autoBackupUri,
        period: String = _state.value.autoBackupPeriod
    ) {
        if (!isEnabled || uri.isEmpty()) return

        val (interval, timeUnit) = when (period) {
            "Daily" -> 1L to TimeUnit.DAYS
            "Weekly" -> 7L to TimeUnit.DAYS
            "Monthly" -> 30L to TimeUnit.DAYS
            else -> 1L to TimeUnit.DAYS
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(false)
            .build()

        val backupRequest = PeriodicWorkRequestBuilder<BackupWorker>(interval, timeUnit)
            .setConstraints(constraints)
            .setInitialDelay(interval, timeUnit)
            .addTag("BACKUP_TAG")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            AUTO_BACKUP_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            backupRequest
        )
    }

    private fun cancelBackup() {
        WorkManager.getInstance(context).cancelUniqueWork(AUTO_BACKUP_WORK_NAME)
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String, val type: SnackbarType) : UiEvent()
    }

    companion object {
        const val AUTO_BACKUP_WORK_NAME = "auto_backup_work"
    }
}
