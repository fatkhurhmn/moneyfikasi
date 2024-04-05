package dev.muffar.moneyfikasi.category.add

import dev.muffar.moneyfikasi.domain.model.CategoryType

sealed class AddCategoryEvent {
    data class OnInitType(val type: CategoryType?) : AddCategoryEvent()
}