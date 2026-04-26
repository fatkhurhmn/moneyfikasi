package dev.muffar.moneyfikasi.export

import dev.muffar.moneyfikasi.domain.model.ExportFormat
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.TemporalAdjusters

data class ExportState(
    val startDate: LocalDateTime = LocalDateTime.now().withDayOfMonth(1),
    val endDate: LocalDateTime = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()),
    val format: ExportFormat = ExportFormat.CSV,
    val isLoading: Boolean = false,
    val message: String? = null
) {
    val fileName: String
        get() = "moneyfikasi_export_${startDate.toLocalDate()}_${endDate.toLocalDate()}.${
            when (format) {
                ExportFormat.CSV -> "csv"
                ExportFormat.XLSX -> "xlsx"
            }
        }"
}