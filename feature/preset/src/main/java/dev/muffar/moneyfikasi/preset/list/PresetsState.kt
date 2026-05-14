package dev.muffar.moneyfikasi.preset.list

import dev.muffar.moneyfikasi.domain.model.Preset
import dev.muffar.moneyfikasi.domain.model.TransactionType

data class PresetsState(
    val presets: List<Preset> = emptyList(),
    val tabs: List<String> = listOf(TransactionType.INCOME.name, TransactionType.EXPENSE.name),
    val isLoading: Boolean = false,
)
