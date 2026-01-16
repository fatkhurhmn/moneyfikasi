package dev.muffar.moneyfikasi.domain.utils

enum class WalletIcon(val iconName: String) {
    ACCOUNT_BALANCE("AccountBalance"),
    ACCOUNT_BALANCE_WALLET("AccountBalanceWallet"),
    CREDIT_CARD("CreditCard"),
    MONETIZATION_ON("MonetizationOn"),
    PAYMENTS("Payments"),
    SAVINGS("Savings"),
    TIMELINE("Timeline"),;

    companion object {
        fun getIcons() = entries.map { it.iconName }
    }
}