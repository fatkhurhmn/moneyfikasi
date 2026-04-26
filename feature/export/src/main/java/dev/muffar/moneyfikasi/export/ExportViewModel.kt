package dev.muffar.moneyfikasi.export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
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
            is ExportEvent.OnStartDateChanged -> {
                _state.update { it.copy(startDate = event.date) }
            }

            is ExportEvent.OnEndDateChanged -> {
                _state.update { it.copy(endDate = event.date) }
            }

            is ExportEvent.OnFormatChanged -> {
                _state.update { it.copy(format = event.format) }
            }

            is ExportEvent.OnExportClick -> {
                viewModelScope.launch {
                    val filename = "transactions_${System.currentTimeMillis()}.${state.value.format.name.lowercase()}"
                    _eventFlow.send(UiEvent.SaveFile(filename, state.value.format))
                }
            }
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
            _state.update { it.copy(isExporting = false, message = "Export successful") }
        } catch (e: Exception) {
            _state.update { it.copy(isExporting = false, message = "Export failed: ${e.message}") }
        }
    }

    sealed class UiEvent {
        data class SaveFile(val filename: String, val format: ExportFormat) : UiEvent()
    }
}
