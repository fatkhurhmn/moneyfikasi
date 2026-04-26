package dev.muffar.moneyfikasi.export

import org.threeten.bp.LocalDateTime

data class ExportState(
    val startDate: LocalDateTime = LocalDateTime.now().withDayOfMonth(1),
    val endDate: LocalDateTime = LocalDateTime.now(),
    val format: ExportFormat = ExportFormat.CSV,
    val isExporting: Boolean = false,
    val message: String? = null
)

enum class ExportFormat {
    CSV, XLSX
}
