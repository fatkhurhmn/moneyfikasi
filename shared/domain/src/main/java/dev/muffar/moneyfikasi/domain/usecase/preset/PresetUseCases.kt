package dev.muffar.moneyfikasi.domain.usecase.preset

data class PresetUseCases(
    val getAllPresets: GetAllPresets,
    val getPresetById: GetPresetById,
    val upsertPreset: UpsertPreset,
    val deletePreset: DeletePreset
)
