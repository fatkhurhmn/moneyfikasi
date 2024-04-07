package dev.muffar.moneyfikasi.category.add

import dev.muffar.moneyfikasi.category.add.component.AddCategoryBottomSheet
import dev.muffar.moneyfikasi.domain.model.CategoryType

sealed class AddCategoryEvent {
    data class OnInitType(val type: CategoryType?) : AddCategoryEvent()
    data class OnNameChange(val name: String) : AddCategoryEvent()
    data class OnIconChange(val icon: String) : AddCategoryEvent()
    data class OnColorChange(val color: Long) : AddCategoryEvent()
    data class OnBottomSheetChange(val type: AddCategoryBottomSheet?) : AddCategoryEvent()
    data object OnSubmitCategory : AddCategoryEvent()
}