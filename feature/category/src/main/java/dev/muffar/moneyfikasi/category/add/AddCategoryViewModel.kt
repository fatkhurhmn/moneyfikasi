package dev.muffar.moneyfikasi.category.add

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.category.add.component.AddCategoryBottomSheet
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.InvalidCategoryException
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases,
) : ViewModel() {

    private val _state = mutableStateOf(AddCategoryState())
    val state: State<AddCategoryState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: AddCategoryEvent) {
        when (event) {
            is AddCategoryEvent.OnInitType -> setType(event.type)
            is AddCategoryEvent.OnNameChange -> onNameChange(event.name)
            is AddCategoryEvent.OnIconChange -> onIconChange(event.icon)
            is AddCategoryEvent.OnColorChange -> onColorChange(event.color)
            is AddCategoryEvent.OnBottomSheetChange -> onBottomSheetChange(event.type)
            is AddCategoryEvent.OnSubmitCategory -> onSubmitCategory()
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

    private fun onSubmitCategory() {
        viewModelScope.launch {
            try {
                val category = Category(
                    id = UUID.randomUUID(),
                    name = state.value.name,
                    icon = state.value.icon,
                    color = state.value.color,
                    type = state.value.type ?: CategoryType.INCOME
                )
                categoryUseCases.saveCategory(category)
                _eventFlow.emit(UiEvent.SaveCategory)
            } catch (e: InvalidCategoryException) {
                _eventFlow.emit(UiEvent.ShowMessage(e.message))
            }
        }
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String) : UiEvent()
        data object SaveCategory : UiEvent()
    }
}