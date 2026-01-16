package dev.muffar.moneyfikasi.domain.model

import dev.muffar.moneyfikasi.utils.constants.UUIDConst
import java.util.UUID

data class Category(
    val id: UUID = UUIDConst.empty,
    val name: String = "",
    val icon: String = "",
    val color: Long = 0,
    val type: CategoryType = CategoryType.INCOME,
    val isActive: Boolean = true,
    val isTransferCategory: Boolean = false
) {
    val isExpense: Boolean
        get() = type == CategoryType.EXPENSE

    val isIncome: Boolean
        get() = type == CategoryType.INCOME

    val isFeeTransfer: Boolean
        get() = id == UUIDConst.TransferFeeCategoryId
}

data class InvalidCategoryException(override val message: String) : Exception()