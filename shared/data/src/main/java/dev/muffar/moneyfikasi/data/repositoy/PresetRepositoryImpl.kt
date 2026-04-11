package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.db.dao.PresetDao
import dev.muffar.moneyfikasi.data.mapper.toDomain
import dev.muffar.moneyfikasi.data.mapper.toEntity
import dev.muffar.moneyfikasi.domain.model.Preset
import dev.muffar.moneyfikasi.domain.repository.PresetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class PresetRepositoryImpl @Inject constructor(
    private val presetDao: PresetDao
) : PresetRepository {
    override fun getAllPresets(): Flow<List<Preset>> {
        return presetDao.getAllPresets().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getPresetById(id: UUID): Preset? {
        return presetDao.getPresetById(id)?.toDomain()
    }

    override suspend fun upsertPreset(preset: Preset) {
        presetDao.upsertPreset(preset.toEntity())
    }

    override suspend fun deletePreset(preset: Preset) {
        presetDao.deletePreset(preset.toEntity())
    }
}
