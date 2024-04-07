package dev.muffar.moneyfikasi.category.add

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.category.add.component.AddCategoryBottomSheet
import dev.muffar.moneyfikasi.domain.model.CategoryType
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor() : ViewModel() {

    private val _state = mutableStateOf(AddCategoryState())
    val state : State<AddCategoryState> = _state

    fun onEvent(event: AddCategoryEvent) {
        when(event) {
            is AddCategoryEvent.OnInitType -> setType(event.type)
            is AddCategoryEvent.OnNameChange -> onNameChange(event.name)
            is AddCategoryEvent.OnIconChange -> onIconChange(event.icon)
            is AddCategoryEvent.OnColorChange -> onColorChange(event.color)
            is AddCategoryEvent.OnBottomSheetChange -> onBottomSheetChange(event.type)
        }
    }

    private fun setType(type: CategoryType?) {
        _state.value = _state.value.copy(type = type)
    }

    private fun onNameChange(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    private fun onIconChange(icon: String) {
        _state.value = _state.value.copy(icon = icon)
    }

    private fun onColorChange(color: Long) {
        _state.value = _state.value.copy(color = color)
    }

    private fun onBottomSheetChange(type: AddCategoryBottomSheet?) {
        _state.value = _state.value.copy(bottomSheetType = type)
    }
}