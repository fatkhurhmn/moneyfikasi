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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BackupRestoreViewModel @Inject constructor(
    private val backupRestoreUseCases: BackupRestoreUseCases,
    private val preferencesUseCases: PreferencesUseCases,
    @ApplicationContext private val context: Context,
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
            backupRestoreUseCases.backupData(uri)
                .onSuccess {
                    preferencesUseCases.setLatestBackup(it, System.currentTimeMillis())
                    _eventFlow.emit(UiEvent.ShowMessage("Backup success", SnackbarType.SUCCESS))
                }
                .onFailure {
                    _eventFlow.emit(UiEvent.ShowMessage("Backup failed: ${it.message}", SnackbarType.ERROR))
                }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun restoreData(uri: Uri) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            backupRestoreUseCases.restoreData(uri)
                .onSuccess {
                    _eventFlow.emit(UiEvent.ShowMessage("Restore success", SnackbarType.SUCCESS))
                }
                .onFailure {
                    _eventFlow.emit(UiEvent.ShowMessage("Restore failed: ${it.message}", SnackbarType.ERROR))
                }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun setAutoBackupEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            preferencesUseCases.setAutoBackupEnabled(isEnabled)
            if (isEnabled) {
                scheduleBackup()
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
                scheduleBackup()
            }
        }
    }

    private fun setAutoBackupPeriod(period: String) {
        viewModelScope.launch {
            preferencesUseCases.setAutoBackupPeriod(period)
            if (_state.value.isAutoBackupEnabled) {
                scheduleBackup()
            }
        }
    }

    private fun scheduleBackup() {
        val uri = _state.value.autoBackupUri
        if (uri.isEmpty()) return

        val period = _state.value.autoBackupPeriod
        val interval = when (period) {
            "Daily" -> 1L
            "Weekly" -> 7L
            "Monthly" -> 30L
            else -> 1L
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(true)
            .build()

        val backupRequest = PeriodicWorkRequestBuilder<BackupWorker>(interval, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            AUTO_BACKUP_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
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
