package dev.muffar.moneyfikasi.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.muffar.moneyfikasi.data.db.entity.PresetEntity
import dev.muffar.moneyfikasi.data.db.entity.PresetWithDetails
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface PresetDao {

    @Transaction
    @Query("SELECT * FROM presets ORDER BY LOWER(name) ASC")
    fun getAllPresets(): Flow<List<PresetWithDetails>>

    @Transaction
    @Query("SELECT * FROM presets WHERE id = :id")
    suspend fun getPresetById(id: UUID): PresetWithDetails?

    @Upsert
    suspend fun upsertPreset(preset: PresetEntity)

    @Delete
    suspend fun deletePreset(preset: PresetEntity)
}
