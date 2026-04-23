package dev.muffar.moneyfikasi.backup_restore

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.usecase.backup_restore.BackupRestoreUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupRestoreViewModel @Inject constructor(
    private val backupRestoreUseCases: BackupRestoreUseCases,
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: BackupRestoreEvent) {
        when (event) {
            is BackupRestoreEvent.OnBackupData -> {
                backupData(event.uri)
            }

            is BackupRestoreEvent.OnRestoreData -> {
                restoreData(event.uri)
            }
        }
    }

    private fun backupData(uri: Uri) {
        viewModelScope.launch {
            backupRestoreUseCases.backupData(uri)
                .onSuccess {
                    _eventFlow.emit(UiEvent.ShowMessage("Backup success", SnackbarType.SUCCESS))
                }
                .onFailure {
                    _eventFlow.emit(UiEvent.ShowMessage("Backup failed", SnackbarType.ERROR))
                }
        }
    }

    private fun restoreData(uri: Uri) {
        viewModelScope.launch {
            backupRestoreUseCases.restoreData(uri)
                .onSuccess {
                    _eventFlow.emit(UiEvent.ShowMessage("Restore success", SnackbarType.SUCCESS))
                }
                .onFailure {
                    _eventFlow.emit(UiEvent.ShowMessage("Restore failed", SnackbarType.ERROR))
                }
        }
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String, val type: SnackbarType) : UiEvent()
    }
}
