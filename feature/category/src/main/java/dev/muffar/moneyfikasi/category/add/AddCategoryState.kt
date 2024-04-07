package dev.muffar.moneyfikasi.category.add

import dev.muffar.moneyfikasi.category.add.component.AddCategoryBottomSheet
import dev.muffar.moneyfikasi.domain.model.CategoryType

data class AddCategoryState(
    val type: CategoryType? = CategoryType.INCOME,
    val name: String = "",
    val icon: String = "",
    val color: Long = 0,
    val bottomSheetType: AddCategoryBottomSheet? = null,
)