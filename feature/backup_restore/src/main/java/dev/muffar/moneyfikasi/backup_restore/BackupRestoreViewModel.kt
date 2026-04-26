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
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.usecase.backup_restore.BackupRestoreUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.PreferencesUseCases
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
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
            preferencesUseCases.getAutoBackupPeriod(),
            preferencesUseCases.isDeletePreviousBackup()
        ) { latestBackup, isAutoBackupEnabled, autoBackupUri, autoBackupPeriod, isDeletePreviousBackup ->
            _state.value = _state.value.copy(
                latestBackupName = latestBackup.name,
                latestBackupDate = latestBackup.date,
                latestBackupFolder = latestBackup.folder,
                isAutoBackupEnabled = isAutoBackupEnabled,
                autoBackupUri = autoBackupUri,
                autoBackupPeriod = autoBackupPeriod,
                isDeletePreviousBackup = isDeletePreviousBackup
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
            is BackupRestoreEvent.OnDeletePreviousBackupChanged -> setDeletePreviousBackup(event.isEnabled)
        }
    }

    private fun backupData(uri: Uri) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val previousBackupName = _state.value.latestBackupName
            delay(500)
            backupRestoreUseCases.backupData(uri)
                .onSuccess { fileName ->
                    if (_state.value.isDeletePreviousBackup && previousBackupName.isNotEmpty()) {
                        backupRestoreUseCases.deleteBackup(uri, previousBackupName)
                    }
                    preferencesUseCases.setLatestBackup(fileName, System.currentTimeMillis(), uri.toString())
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
            delay(500)
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
            if (uri.scheme == "content") {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            preferencesUseCases.setAutoBackupUri(uri.toString())
            if (_state.value.isAutoBackupEnabled) {
                scheduleBackup(uri = uri.toString())
            }
        }
    }

    private fun setAutoBackupPeriod(period: TimePeriod) {
        viewModelScope.launch {
            preferencesUseCases.setAutoBackupPeriod(period)
            if (_state.value.isAutoBackupEnabled) {
                scheduleBackup(period = period)
            }
        }
    }

    private fun setDeletePreviousBackup(isEnabled: Boolean) {
        viewModelScope.launch {
            preferencesUseCases.setDeletePreviousBackup(isEnabled)
        }
    }

    private fun scheduleBackup(
        isEnabled: Boolean = _state.value.isAutoBackupEnabled,
        uri: String = _state.value.autoBackupUri,
        period: TimePeriod = _state.value.autoBackupPeriod
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
