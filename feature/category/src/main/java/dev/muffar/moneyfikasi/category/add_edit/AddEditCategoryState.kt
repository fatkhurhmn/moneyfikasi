package dev.muffar.moneyfikasi.category.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import java.util.UUID

data class AddEditCategoryState(
    val type: CategoryType = CategoryType.INCOME,
    val id: UUID? = null,
    val name: String = "",
    val icon: String = "",
    val color: Long = 0,
    val isActive: Boolean = true,
    val showAlert: Boolean = false
) {
    val category: Category
        get() = Category(
            id = id ?: UUID.randomUUID(),
            name = name.trim(),
            icon = icon,
            color = color,
            type = type,
            isActive = isActive
        )
}