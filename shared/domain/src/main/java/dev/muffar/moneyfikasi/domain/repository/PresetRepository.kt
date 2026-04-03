package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.Preset
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface PresetRepository {
    fun getAllPresets(): Flow<List<Preset>>
    suspend fun getPresetById(id: UUID): Preset?
    suspend fun upsertPreset(preset: Preset)
    suspend fun deletePreset(preset: Preset)
}
