package dev.muffar.moneyfikasi.export

import dev.muffar.moneyfikasi.domain.model.ExportFormat

sealed class ExportEvent {
    data class StartDateChanged(val date: Long) : ExportEvent()
    data class EndDateChanged(val date: Long) : ExportEvent()
    data class FormatChanged(val format: ExportFormat) : ExportEvent()
}
