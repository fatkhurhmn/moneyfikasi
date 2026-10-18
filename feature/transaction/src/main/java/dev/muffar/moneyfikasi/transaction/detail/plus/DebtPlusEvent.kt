package dev.muffar.moneyfikasi.transaction.detail.plus

import dev.muffar.moneyfikasi.domain.model.Wallet

sealed class DebtPlusEvent {
    data class TabSelected(val index: Int) : DebtPlusEvent()
    data class AmountChanged(val amount: String) : DebtPlusEvent()
    data class WalletSelected(val wallet: Wallet) : DebtPlusEvent()
    data class DateSelected(val date: Long) : DebtPlusEvent()
    data object SavePlus : DebtPlusEvent()
}
