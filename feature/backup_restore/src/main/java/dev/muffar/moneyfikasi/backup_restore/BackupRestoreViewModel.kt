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
import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.usecase.backup_restore.BackupRestoreUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.backup.BackupSettingsUseCases
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BackupRestoreViewModel @Inject constructor(
    private val backupRestoreUseCases: BackupRestoreUseCases,
    private val backupSettingsUseCases: BackupSettingsUseCases,
    @param:ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state = mutableStateOf(BackupRestoreState())
    val state: State<BackupRestoreState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        backupSettingsUseCases.getBackupSettings().onEach { settings ->
            _state.value = _state.value.copy(
                latestBackup = settings.latestBackup,
                autoBackup = settings.autoBackup,
                isDeletePreviousBackup = settings.isDeletePreviousBackup
            )
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: BackupRestoreEvent) {
        when (event) {
            is BackupRestoreEvent.BackupData -> backupData(event.uri)
            is BackupRestoreEvent.RestoreData -> restoreData(event.uri)
            is BackupRestoreEvent.AutoBackupEnabledChanged -> setAutoBackupEnabled(event.isEnabled)
            is BackupRestoreEvent.AutoBackupUriChanged -> setAutoBackupUri(event.uri)
            is BackupRestoreEvent.AutoBackupPeriodChanged -> setAutoBackupPeriod(event.period)
            is BackupRestoreEvent.DeletePreviousBackupChanged -> setDeletePreviousBackup(event.isEnabled)
        }
    }

    private fun backupData(uri: Uri) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val previousBackup = _state.value.latestBackup
            delay(200)
            backupRestoreUseCases.backupData(uri)
                .onSuccess { fileName ->
                    if (_state.value.isDeletePreviousBackup && previousBackup.name.isNotEmpty() && previousBackup.folder.isNotEmpty()) {
                        backupRestoreUseCases.deleteBackup(previousBackup)
                    }
                    val latestBackup = LatestBackup(
                        name = fileName,
                        date = System.currentTimeMillis(),
                        folder = uri.toString()
                    )
                    backupSettingsUseCases.setLatestBackup(latestBackup)
                    _eventFlow.emit(UiEvent.ShowMessage("Backup success", SnackbarType.SUCCESS))
                }
                .onFailure {
                    _eventFlow.emit(
                        UiEvent.ShowMessage(
                            "Backup failed: ${it.message}",
                            SnackbarType.ERROR,
                        )
                    )
                }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun restoreData(uri: Uri) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            delay(200)
            backupRestoreUseCases.restoreData(uri)
                .onSuccess {
                    _eventFlow.emit(UiEvent.ShowMessage("Restore success", SnackbarType.SUCCESS))
                }
                .onFailure {
                    _eventFlow.emit(
                        UiEvent.ShowMessage(
                            "Restore failed: ${it.message}",
                            SnackbarType.ERROR,
                        )
                    )
                }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun setAutoBackupEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            backupSettingsUseCases.setAutoBackupEnabled(isEnabled)
            if (isEnabled) {
                scheduleBackup(isEnabled = true)
            } else {
                cancelBackup()
            }
        }
    }

    private fun setAutoBackupUri(uri: Uri) {
        viewModelScope.launch {
            if (uri.scheme == "content") {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            backupSettingsUseCases.setAutoBackupUri(uri.toString())
            if (_state.value.autoBackup.isEnabled) {
                scheduleBackup(uri = uri.toString())
            }
        }
    }

    private fun setAutoBackupPeriod(period: TimePeriod) {
        viewModelScope.launch {
            backupSettingsUseCases.setAutoBackupPeriod(period)
            if (_state.value.autoBackup.isEnabled) {
                scheduleBackup(period = period)
            }
        }
    }

    private fun setDeletePreviousBackup(isEnabled: Boolean) {
        viewModelScope.launch {
            backupSettingsUseCases.setDeletePreviousBackup(isEnabled)
        }
    }

    private fun scheduleBackup(
        isEnabled: Boolean = _state.value.autoBackup.isEnabled,
        uri: String = _state.value.autoBackup.uri,
        period: TimePeriod = TimePeriod.valueOf(_state.value.autoBackup.period)
    ) {
        if (!isEnabled || uri.isEmpty()) return

        val (interval, timeUnit) = when (period) {
            TimePeriod.DAILY -> 1L to TimeUnit.DAYS
            TimePeriod.WEEKLY -> 7L to TimeUnit.DAYS
            TimePeriod.MONTHLY -> 30L to TimeUnit.DAYS
            else -> 1L to TimeUnit.DAYS
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(requiresCharging = false)
            .setRequiresBatteryNotLow(requiresBatteryNotLow = false)
            .build()

        val currentTime = LocalDateTime.now()
        val nextMidnight = currentTime.plusDays(1).with(LocalTime.MIDNIGHT)
        val initialDelay = Duration.between(currentTime, nextMidnight).toMillis()

        val backupRequest = PeriodicWorkRequestBuilder<BackupWorker>(interval, timeUnit)
            .setConstraints(constraints)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
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