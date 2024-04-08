package dev.muffar.moneyfikasi.category.add_edit

import dev.muffar.moneyfikasi.category.add_edit.component.AddEditCategoryBottomSheet
import dev.muffar.moneyfikasi.domain.model.CategoryType

sealed class AddEditCategoryEvent {
    data class OnInitType(val type: CategoryType?) : AddEditCategoryEvent()
    data class OnNameChange(val name: String) : AddEditCategoryEvent()
    data class OnIconChange(val icon: String) : AddEditCategoryEvent()
    data class OnColorChange(val color: Long) : AddEditCategoryEvent()
    data object OnIsActiveChange : AddEditCategoryEvent()
    data class OnBottomSheetChange(val type: AddEditCategoryBottomSheet?) : AddEditCategoryEvent()
    data object OnSubmitEditCategory : AddEditCategoryEvent()
}