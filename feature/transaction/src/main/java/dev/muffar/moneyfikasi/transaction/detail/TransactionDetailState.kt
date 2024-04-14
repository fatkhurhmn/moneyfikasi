package dev.muffar.moneyfikasi.transaction.detail

import dev.muffar.moneyfikasi.domain.model.Transaction

data class TransactionDetailState(
    val transaction: Transaction? = null,
    val showAlert : Boolean = false
)