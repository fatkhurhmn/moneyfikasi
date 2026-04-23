package dev.muffar.moneyfikasi.backup_restore

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
                    _eventFlow.emit(UiEvent.ShowMessage("Backup Success"))
                }
                .onFailure {
                    _eventFlow.emit(UiEvent.ShowMessage("Backup Failed: ${it.message}"))
                }
        }
    }

    private fun restoreData(uri: Uri) {
        viewModelScope.launch {
            backupRestoreUseCases.restoreData(uri)
                .onSuccess {
                    _eventFlow.emit(UiEvent.ShowMessage("Restore Success"))
                }
                .onFailure {
                    _eventFlow.emit(UiEvent.ShowMessage("Restore Failed: ${it.message}"))
                }
        }
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String) : UiEvent()
    }
}
