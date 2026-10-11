package dev.muffar.moneyfikasi.backup_restore

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.model.DriveSignInResult
import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.usecase.backup_restore.BackupRestoreUseCases
import dev.muffar.moneyfikasi.domain.usecase.drive.DriveBackupUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.backup.BackupSettingsUseCases
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupRestoreViewModel @Inject constructor(
    private val backupRestoreUseCases: BackupRestoreUseCases,
    private val backupSettingsUseCases: BackupSettingsUseCases,
    private val driveBackupUseCases: DriveBackupUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(BackupRestoreState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        backupSettingsUseCases.getBackupSettings().onEach { settings ->
            _state.update {
                it.copy(
                    latestBackup = settings.latestBackup,
                    autoBackup = settings.autoBackup,
                    isDeletePreviousBackup = settings.isDeletePreviousBackup,
                    isDriveAutoBackupEnabled = settings.isDriveAutoBackupEnabled,
                    driveAutoBackupPeriod = settings.driveAutoBackupPeriod
                )
            }
        }.launchIn(viewModelScope)
        refreshDriveStatus()
    }

    fun requestDriveSignIn(activity: Activity? = null) {
        viewModelScope.launch {
            when (val result = driveBackupUseCases.signInToDrive(activity)) {
                is DriveSignInResult.Success -> refreshDriveStatus(loadList = true)
                is DriveSignInResult.NeedsAuthorization -> {
                    val pi = result.pendingIntent
                    if (pi != null) {
                        _eventFlow.emit(UiEvent.RequestDriveAuthorization(pi))
                    } else {
                        _eventFlow.emit(UiEvent.ShowMessage(R.string.error_backup_failed, SnackbarType.ERROR))
                    }
                }
                is DriveSignInResult.Failure -> {
                    if (!driveBackupUseCases.isDriveSignedIn()) {
                        _eventFlow.emit(UiEvent.ShowMessage(R.string.error_backup_failed, SnackbarType.ERROR))
                    } else {
                        refreshDriveStatus(loadList = true)
                    }
                }
            }
        }
    }

    fun onDriveAuthorizationResult(granted: Boolean) {
        viewModelScope.launch {
            if (granted) {
                refreshDriveStatus(loadList = true)
            } else {
                _eventFlow.emit(UiEvent.ShowMessage(R.string.error_backup_failed, SnackbarType.ERROR))
            }
        }
    }

    fun onEvent(event: BackupRestoreEvent) {
        when (event) {
            is BackupRestoreEvent.BackupData -> backupData(event.uri)
            is BackupRestoreEvent.RestoreData -> restoreData(event.uri)
            is BackupRestoreEvent.AutoBackupEnabledChanged -> setAutoBackupEnabled(event.isEnabled)
            is BackupRestoreEvent.AutoBackupUriChanged -> setAutoBackupUri(event.uri)
            is BackupRestoreEvent.AutoBackupPeriodChanged -> setAutoBackupPeriod(event.period)
            is BackupRestoreEvent.DeletePreviousBackupChanged -> setDeletePreviousBackup(event.isEnabled)
            is BackupRestoreEvent.DriveSignInResult -> refreshDriveStatus(loadList = true)
            is BackupRestoreEvent.DriveSignInHandled -> onDriveSignInHandled(event.success)
            is BackupRestoreEvent.DriveSignOut -> driveSignOut()
            is BackupRestoreEvent.DriveLoadBackups -> loadDriveBackups()
            is BackupRestoreEvent.DriveBackupNow -> backupToDrive()
            is BackupRestoreEvent.DriveRestore -> restoreFromDrive()
            is BackupRestoreEvent.DriveAutoBackupChanged -> setDriveAutoBackupEnabled(event.isEnabled)
            is BackupRestoreEvent.DriveAutoBackupPeriodChanged -> setDriveAutoBackupPeriod(event.period)
        }
    }

    private fun refreshDriveStatus(loadList: Boolean = true) {
        viewModelScope.launch {
            val signedIn = driveBackupUseCases.isDriveSignedIn()
            val email = driveBackupUseCases.getDriveAccountEmail()
            _state.update {
                it.copy(
                    isDriveSignedIn = signedIn,
                    driveAccountEmail = email
                )
            }
            if (signedIn && loadList) loadDriveBackups()
        }
    }

    private fun onDriveSignInHandled(success: Boolean) {
        viewModelScope.launch {
            if (success) {
                refreshDriveStatus(loadList = true)
            } else {
                _eventFlow.emit(
                    UiEvent.ShowMessage(
                        R.string.error_backup_failed,
                        SnackbarType.ERROR
                    )
                )
            }
        }
    }

    private fun driveSignOut() {
        viewModelScope.launch {
            driveBackupUseCases.driveSignOut()
            backupSettingsUseCases.setDriveAutoBackupEnabled(false)
            backupRestoreUseCases.cancelDriveAutoBackup()
            _state.update {
                it.copy(
                    isDriveSignedIn = false,
                    driveAccountEmail = "",
                    driveBackup = null,
                    isDriveAutoBackupEnabled = false
                )
            }
        }
    }

    private fun setDriveAutoBackupEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            if (isEnabled && !driveBackupUseCases.isDriveSignedIn()) return@launch
            backupSettingsUseCases.setDriveAutoBackupEnabled(isEnabled)
            if (isEnabled) {
                backupRestoreUseCases.scheduleDriveAutoBackup(
                    isSignedIn = _state.value.isDriveSignedIn,
                    period = TimePeriod.valueOf(_state.value.driveAutoBackupPeriod)
                )
            } else {
                backupRestoreUseCases.cancelDriveAutoBackup()
            }
        }
    }

    private fun loadDriveBackups() {
        viewModelScope.launch {
            _state.update { it.copy(isDriveLoading = true) }
            driveBackupUseCases.getDriveBackups()
                .onSuccess { files ->
                    _state.update {
                        it.copy(
                            driveBackup = files.maxByOrNull { it.modifiedTime },
                            isDriveLoading = false
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isDriveLoading = false) }
                    _eventFlow.emit(
                        UiEvent.ShowMessage(
                            R.string.error_backup_failed,
                            SnackbarType.ERROR
                        )
                    )
                }
        }
    }

    private fun backupToDrive() {
        viewModelScope.launch {
            _state.update { it.copy(isDriveLoading = true) }
            driveBackupUseCases.backupToDrive()
                .onSuccess { file ->
                    _state.update { it.copy(driveBackup = file, isDriveLoading = false) }
                    _eventFlow.emit(
                        UiEvent.ShowMessage(
                            R.string.msg_backup_success,
                            SnackbarType.SUCCESS
                        )
                    )
                }
                .onFailure {
                    _state.update { it.copy(isDriveLoading = false) }
                    _eventFlow.emit(
                        UiEvent.ShowMessage(
                            R.string.error_backup_failed,
                            SnackbarType.ERROR
                        )
                    )
                }
        }
    }

    private fun restoreFromDrive() {
        val fileId = _state.value.driveBackup?.id ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            driveBackupUseCases.restoreFromDrive(fileId)
                .onSuccess {
                    _eventFlow.emit(
                        UiEvent.ShowMessage(
                            R.string.msg_restore_success,
                            SnackbarType.SUCCESS
                        )
                    )
                }
                .onFailure {
                    _eventFlow.emit(
                        UiEvent.ShowMessage(
                            R.string.error_restore_failed,
                            SnackbarType.ERROR
                        )
                    )
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun backupData(uri: Uri) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
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
                    _eventFlow.emit(
                        UiEvent.ShowMessage(
                            R.string.msg_backup_success,
                            SnackbarType.SUCCESS
                        )
                    )
                }
                .onFailure {
                    _eventFlow.emit(
                        UiEvent.ShowMessage(
                            R.string.error_backup_failed,
                            SnackbarType.ERROR,
                        )
                    )
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun restoreData(uri: Uri) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(200)
            backupRestoreUseCases.restoreData(uri)
                .onSuccess {
                    _eventFlow.emit(
                        UiEvent.ShowMessage(
                            R.string.msg_restore_success,
                            SnackbarType.SUCCESS
                        )
                    )
                }
                .onFailure {
                    _eventFlow.emit(
                        UiEvent.ShowMessage(
                            R.string.error_restore_failed,
                            SnackbarType.ERROR
                        )
                    )
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun setAutoBackupEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            backupSettingsUseCases.setAutoBackupEnabled(isEnabled)
            if (isEnabled) {
                backupRestoreUseCases.scheduleAutoBackup(
                    isEnabled = true,
                    uri = _state.value.autoBackup.uri,
                    period = TimePeriod.valueOf(_state.value.autoBackup.period)
                )
            } else {
                backupRestoreUseCases.cancelAutoBackup()
            }
        }
    }

    private fun setAutoBackupUri(uri: Uri) {
        viewModelScope.launch {
            backupRestoreUseCases.takePersistableUriPermission(uri)
            backupSettingsUseCases.setAutoBackupUri(uri.toString())
            if (_state.value.autoBackup.isEnabled) {
                backupRestoreUseCases.scheduleAutoBackup(
                    isEnabled = true,
                    uri = uri.toString(),
                    period = TimePeriod.valueOf(_state.value.autoBackup.period)
                )
            }
        }
    }

    private fun setAutoBackupPeriod(period: TimePeriod) {
        viewModelScope.launch {
            backupSettingsUseCases.setAutoBackupPeriod(period)
            if (_state.value.autoBackup.isEnabled) {
                backupRestoreUseCases.scheduleAutoBackup(
                    isEnabled = true,
                    uri = _state.value.autoBackup.uri,
                    period = period
                )
            }
        }
    }

    private fun setDriveAutoBackupPeriod(period: TimePeriod) {
        viewModelScope.launch {
            backupSettingsUseCases.setDriveAutoBackupPeriod(period)
            if (_state.value.isDriveAutoBackupEnabled) {
                backupRestoreUseCases.scheduleDriveAutoBackup(
                    isSignedIn = _state.value.isDriveSignedIn,
                    period = period
                )
            }
        }
    }

    private fun setDeletePreviousBackup(isEnabled: Boolean) {
        viewModelScope.launch {
            backupSettingsUseCases.setDeletePreviousBackup(isEnabled)
        }
    }

    sealed class UiEvent {
        data class ShowMessage(
            val messageResId: Int,
            val type: SnackbarType,
            val formatArg: String? = null
        ) : UiEvent()
        data class RequestDriveAuthorization(val pendingIntent: android.app.PendingIntent) : UiEvent()
    }
}
