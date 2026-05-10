package dev.muffar.moneyfikasi.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class CategoryStatisticEntity(
    @Embedded val category: CategoryEntity,
    @ColumnInfo(name = "total_amount") val totalAmount: Double,
    @ColumnInfo(name = "transaction_count") val transactionCount: Int
)
