package dev.muffar.moneyfikasi.data.db

import androidx.room.TypeConverter
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.LoanType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import java.util.UUID

class RoomTypeConverters {

    @TypeConverter
    fun saveUUID(uuid: UUID): String = uuid.toString()

    @TypeConverter
    fun parseUUID(uuid: String): UUID = UUID.fromString(uuid)

    @TypeConverter
    fun saveDate(date: LocalDateTime): Long = date.toEpochSecond(ZoneOffset.UTC)

    @TypeConverter
    fun parseDate(date: Long): LocalDateTime = LocalDateTime.ofEpochSecond(date, 0, ZoneOffset.UTC)

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