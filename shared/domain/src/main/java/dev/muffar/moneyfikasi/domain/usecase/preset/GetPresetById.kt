package dev.muffar.moneyfikasi.domain.usecase.preset

import dev.muffar.moneyfikasi.domain.model.Preset
import dev.muffar.moneyfikasi.domain.repository.PresetRepository
import java.util.UUID

class GetPresetById(
    private val repository: PresetRepository
) {
    suspend operator fun invoke(id: UUID): Preset? {
        return repository.getPresetById(id)
    }
}
