package dev.muffar.moneyfikasi.data.db

import androidx.room.TypeConverter
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.LoanType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.UUID

class RoomTypeConverters {

    @TypeConverter
    fun saveUUID(uuid: UUID): String = uuid.toString()

    @TypeConverter
    fun parseUUID(uuid: String): UUID = UUID.fromString(uuid)

    @TypeConverter
    fun saveDate(date: LocalDateTime): Long =
        date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    @TypeConverter
    fun parseDate(date: Long): LocalDateTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault())

    @TypeConverter
    fun saveCategoryType(type: CategoryType): String = type.name

    @TypeConverter
    fun parseCategoryType(type: String): CategoryType = CategoryType.valueOf(type)

    @TypeConverter
    fun saveLoanType(type: LoanType): String = type.name

    @TypeConverter
    fun parseLoanType(type: String): LoanType = LoanType.valueOf(type)

    @TypeConverter
    fun saveTransactionType(type: TransactionType): String = type.name

    @TypeConverter
    fun parseTransactionType(type: String): TransactionType = TransactionType.valueOf(type)
}