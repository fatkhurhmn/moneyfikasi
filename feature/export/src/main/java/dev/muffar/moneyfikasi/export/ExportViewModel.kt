package dev.muffar.moneyfikasi.export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.model.ExportFormat
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.export.utils.ExportManager
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfDay
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
) : ViewModel() {

    init {
        System.setProperty("org.apache.poi.util.XMLHelper.feature.secure-processing", "false")
    }

    private val _state = MutableStateFlow(ExportState())
    val state = _state.asStateFlow()

    private val _eventFlow = Channel<UiEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private val exportManager = ExportManager()

    fun onEvent(event: ExportEvent) {
        when (event) {
            is ExportEvent.OnStartDateChanged -> onStartDateChanged(event.date)
            is ExportEvent.OnEndDateChanged -> onEndDateChanged(event.date)
            is ExportEvent.OnFormatChanged -> onFormatChanged(event.format)
            is ExportEvent.OnExportClick -> onExportClick()
        }
    }

    private fun onStartDateChanged(date: LocalDateTime) {
        _state.update { it.copy(startDate = date) }
    }

    private fun onEndDateChanged(date: LocalDateTime) {
        _state.update { it.copy(endDate = date) }
    }

    private fun onFormatChanged(format: ExportFormat) {
        _state.update { it.copy(format = format) }
    }

    private fun onExportClick() {
        viewModelScope.launch {
            val filename =
                "transactions_${System.currentTimeMillis()}.${state.value.format.name.lowercase()}"
            _eventFlow.send(UiEvent.SaveFile(filename, state.value.format))
        }
    }

    suspend fun exportTransactions(outputStream: OutputStream) {
        _state.update { it.copy(isExporting = true) }
        try {
            val transactions = transactionUseCases.getAllTransactions(
                state.value.startDate.startOfDay(),
                state.value.endDate.endOfDay(),
                emptySet(),
                emptySet()
            ).first()

            withContext(Dispatchers.IO) {
                if (state.value.format == ExportFormat.CSV) {
                    exportManager.exportToCsv(transactions, outputStream)
                } else {
                    exportManager.exportToExcel(transactions, outputStream)
                }
            }
            _eventFlow.send(UiEvent.ShowMessage("Export successful", SnackbarType.SUCCESS))
        } catch (e: Exception) {
            _eventFlow.send(UiEvent.ShowMessage("Export failed: ${e.message}", SnackbarType.ERROR))
        } finally {
            _state.update { it.copy(isExporting = false) }
        }
    }

    sealed class UiEvent {
        data class SaveFile(val filename: String, val format: ExportFormat) : UiEvent()
        data class ShowMessage(val message: String, val type: SnackbarType) : UiEvent()
    }
}
