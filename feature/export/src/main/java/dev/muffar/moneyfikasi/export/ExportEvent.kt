package dev.muffar.moneyfikasi.export

import dev.muffar.moneyfikasi.domain.model.ExportFormat

sealed class ExportEvent {
    data class OnStartDateChanged(val date: Long) : ExportEvent()
    data class OnEndDateChanged(val date: Long) : ExportEvent()
    data class OnFormatChanged(val format: ExportFormat) : ExportEvent()
}
