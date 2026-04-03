package dev.muffar.moneyfikasi.preset.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet

sealed class AddEditPresetEvent {
    data class InitType(val type: TransactionType) : AddEditPresetEvent()
    data class NameChanged(val name: String) : AddEditPresetEvent()
    data class AmountChanged(val amount: String) : AddEditPresetEvent()
    data class CategoryChanged(val category: Category?) : AddEditPresetEvent()
    data class WalletChanged(val wallet: Wallet?) : AddEditPresetEvent()
    data class DescriptionChanged(val description: String) : AddEditPresetEvent()
    data object SavePreset : AddEditPresetEvent()
    data object DeletePreset : AddEditPresetEvent()
    data class ShowDeleteAlert(val show: Boolean) : AddEditPresetEvent()
}
