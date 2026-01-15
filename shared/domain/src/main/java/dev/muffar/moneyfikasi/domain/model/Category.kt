package dev.muffar.moneyfikasi.domain.model

import java.util.UUID

data class Category(
    val id: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000"),
    val name: String = "",
    val icon: String = "",
    val color: Long = 0,
    val type: CategoryType = CategoryType.INCOME,
    val isActive: Boolean = true,
    val isTransferCategory: Boolean = false
)

data class InvalidCategoryException(override val message: String) : Exception()