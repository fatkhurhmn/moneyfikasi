package dev.muffar.moneyfikasi.domain.usecase.preset

import dev.muffar.moneyfikasi.domain.model.Preset
import dev.muffar.moneyfikasi.domain.repository.PresetRepository

class UpsertPreset(
    private val repository: PresetRepository
) {
    suspend operator fun invoke(preset: Preset) {
        repository.upsertPreset(preset)
    }
}
