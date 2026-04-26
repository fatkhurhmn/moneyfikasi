package dev.muffar.moneyfikasi.export

import dev.muffar.moneyfikasi.domain.model.ExportFormat
import org.threeten.bp.LocalDateTime

sealed class ExportEvent {
    data class OnStartDateChanged(val date: LocalDateTime) : ExportEvent()
    data class OnEndDateChanged(val date: LocalDateTime) : ExportEvent()
    data class OnFormatChanged(val format: ExportFormat) : ExportEvent()
    object OnExportClick : ExportEvent()
}
