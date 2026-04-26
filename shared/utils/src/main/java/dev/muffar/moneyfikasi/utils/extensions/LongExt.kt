package dev.muffar.moneyfikasi.utils.extensions

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LongExt {
    fun Long.toFormattedDateTime(pattern: String): String {
        val date = Date(this)
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(date)
    }

    fun Long.toLocalDateTime(): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    }
}
