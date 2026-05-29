package dev.muffar.moneyfikasi.export

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.model.ExportFormat
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.export.utils.ExportManager
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfDay
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfDay
import dev.muffar.moneyfikasi.utils.extensions.LongExt.toLocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(ExportState())
    val state = _state.asStateFlow()

    private val _eventFlow = Channel<UiEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private val exportManager = ExportManager()

    fun onEvent(event: ExportEvent) {
        when (event) {
            is ExportEvent.StartDateChanged -> onStartDateChange(event.date)
            is ExportEvent.EndDateChanged -> onEndDateChange(event.date)
            is ExportEvent.FormatChanged -> onFormatChange(event.format)
        }
    }

    private fun onStartDateChange(date: Long) {
        _state.update { it.copy(startDate = date.toLocalDateTime()) }
    }

    private fun onEndDateChange(date: Long) {
        _state.update { it.copy(endDate = date.toLocalDateTime()) }
    }

    private fun onFormatChange(format: ExportFormat) {
        _state.update { it.copy(format = format) }
    }

    suspend fun exportTransactions(outputStream: OutputStream) {
        _state.update { it.copy(isLoading = true) }
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
            _eventFlow.send(UiEvent.ShowMessage(R.string.msg_export_success, SnackbarType.SUCCESS))
        } catch (e: Exception) {
            _eventFlow.send(
                UiEvent.ShowMessage(
                    R.string.error_export_failed,
                    SnackbarType.ERROR,
                    e.message.orEmpty()
                )
            )
        } finally {
            _state.update { it.copy(isLoading = false) }
        }
    }

    sealed class UiEvent {
        data class ShowMessage(
            val messageResId: Int,
            val type: SnackbarType,
            val formatArg: String? = null
        ) : UiEvent()
    }
}
