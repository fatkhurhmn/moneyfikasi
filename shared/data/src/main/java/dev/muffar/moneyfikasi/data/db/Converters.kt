package dev.muffar.moneyfikasi.data.db

import androidx.room.TypeConverter
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TransactionType
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.UUID

class Converters {

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return uuid?.let { UUID.fromString(it) }
    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun toLocalDateTime(millis: Long?): LocalDateTime? {
        return millis?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
        }
    }

    @TypeConverter
    fun fromTransactionType(type: TransactionType): String {
        return type.name
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }

    @TypeConverter
    fun fromCategoryType(type: CategoryType): String {
        return type.name
    }

    @TypeConverter
    fun toCategoryType(value: String): CategoryType {
        return CategoryType.valueOf(value)
    }

    @TypeConverter
    fun fromTimePeriod(timePeriod: TimePeriod): String {
        return timePeriod.name
    }

    @TypeConverter
    fun toTimePeriod(value: String): TimePeriod {
        return TimePeriod.valueOf(value)
    }

    @TypeConverter
    fun fromRecurringEndType(recurringEndType: RecurringEndType): String {
        return recurringEndType.name
    }

    @TypeConverter
    fun toRecurringEndType(value: String): RecurringEndType {
        return RecurringEndType.valueOf(value)
    }
}
