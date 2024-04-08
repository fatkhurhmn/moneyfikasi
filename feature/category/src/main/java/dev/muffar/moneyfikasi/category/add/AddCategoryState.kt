package dev.muffar.moneyfikasi.category.add

import dev.muffar.moneyfikasi.category.add.component.AddCategoryBottomSheet
import dev.muffar.moneyfikasi.domain.model.CategoryType
import java.util.UUID

data class AddCategoryState(
    val type: CategoryType? = CategoryType.INCOME,
    val id : UUID? = null,
    val name: String = "",
    val icon: String = "",
    val color: Long = 0,
    val isActive : Boolean = true,
    val bottomSheetType: AddCategoryBottomSheet? = null,
)