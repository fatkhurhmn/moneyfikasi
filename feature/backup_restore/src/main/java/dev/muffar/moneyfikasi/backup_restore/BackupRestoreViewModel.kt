package dev.muffar.moneyfikasi.backup_restore

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
                backupData()
            }

            is BackupRestoreEvent.OnRestoreData -> {
                backupRestoreUseCases.restoreData()
            }
        }
    }

    private fun backupData() {
        viewModelScope.launch {
            val result = backupRestoreUseCases.backupData()
            if (result == 0) {
                _eventFlow.emit(UiEvent.ShowMessage("Backup Success"))
            } else {
                _eventFlow.emit(UiEvent.ShowMessage("Backup Failed"))
            }
        }
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String) : UiEvent()
    }
}