package dev.muffar.moneyfikasi.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.muffar.moneyfikasi.domain.model.TransactionType
import java.util.UUID

@Entity(
    tableName = "presets",
    foreignKeys = [
        ForeignKey(
            entity = WalletEntity::class,
            parentColumns = ["id"],
            childColumns = ["wallet_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PresetEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "type")
    val type: TransactionType,

    @ColumnInfo(name = "wallet_id")
    val walletId: UUID,

    @ColumnInfo(name = "category_id")
    val categoryId: UUID,

    @ColumnInfo(name = "note")
    val note: String? = null
)
