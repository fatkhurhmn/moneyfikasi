package dev.muffar.moneyfikasi.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.muffar.moneyfikasi.domain.model.TransactionType
import org.threeten.bp.LocalDateTime
import java.util.UUID

@Entity(tableName = "transactions")
data class TransactionEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID,

    @ColumnInfo(name = "wallet_id")
    val walletId: UUID,

    @ColumnInfo(name = "category_id")
    val categoryId: UUID,

    @ColumnInfo(name = "type")
    val type: TransactionType,

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "date")
    val date: LocalDateTime,

    @ColumnInfo(name = "note")
    val note: String? = null,
)