package dev.muffar.moneyfikasi.domain.usecase.preset

import dev.muffar.moneyfikasi.domain.model.Preset
import dev.muffar.moneyfikasi.domain.repository.PresetRepository
import kotlinx.coroutines.flow.Flow

class GetAllPresets(
    private val repository: PresetRepository
) {
    operator fun invoke(): Flow<List<Preset>> {
        return repository.getAllPresets()
    }
}
