package dev.muffar.moneyfikasi.backup_restore

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.usecase.backup_restore.BackupRestoreUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.PreferencesUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupRestoreViewModel @Inject constructor(
    private val backupRestoreUseCases: BackupRestoreUseCases,
    private val preferencesUseCases: PreferencesUseCases,
) : ViewModel() {

    private val _state = mutableStateOf(BackupRestoreState())
    val state: State<BackupRestoreState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        preferencesUseCases.getLatestBackup()
            .onEach {
                _state.value = _state.value.copy(
                    latestBackupName = it.name,
                    latestBackupDate = it.date
                )
            }
            .launchIn(viewModelScope)
    }

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

    sealed class UiEvent {
        data class ShowMessage(val message: String, val type: SnackbarType) : UiEvent()
    }
}
