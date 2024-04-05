package dev.muffar.moneyfikasi.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.muffar.moneyfikasi.domain.model.CategoryType
import java.util.UUID

@Entity(tableName = "categories")
data class CategoryEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "icon")
    val icon: String,

    @ColumnInfo(name = "type")
    val type: CategoryType,

    @ColumnInfo(name = "is_active")
    val isActive : Boolean = true
)
