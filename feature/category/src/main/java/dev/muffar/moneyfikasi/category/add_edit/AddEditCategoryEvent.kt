package dev.muffar.moneyfikasi.category.add_edit

import dev.muffar.moneyfikasi.domain.model.CategoryType

sealed class AddEditCategoryEvent {
    data class TypeChanged(val type: CategoryType) : AddEditCategoryEvent()
    data class NameChanged(val name: String) : AddEditCategoryEvent()
    data class IconChanged(val icon: String) : AddEditCategoryEvent()
    data class ColorChanged(val color: Long) : AddEditCategoryEvent()
    data object CategoryActivated : AddEditCategoryEvent()
    data class ShowDeleteAlert(val showAlert: Boolean) : AddEditCategoryEvent()
    data object SaveCategory : AddEditCategoryEvent()
    data object DeleteCategory : AddEditCategoryEvent()
}