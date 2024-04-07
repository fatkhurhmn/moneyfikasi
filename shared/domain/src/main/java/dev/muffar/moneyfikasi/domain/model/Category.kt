package dev.muffar.moneyfikasi.domain.model

import java.util.UUID

data class Category(
    val id: UUID,
    val name: String,
    val icon: String,
    val color: Long,
    val type: CategoryType,
    val isActive : Boolean = true
)

data class InvalidCategoryException(override val message: String) : Exception()