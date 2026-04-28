package dev.muffar.moneyfikasi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UiSettings(
    val isBalanceVisible: Boolean = false,
    val isReportVisible: Boolean = false
)