package dev.muffar.moneyfikasi.category.add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.category.add_edit.component.AddEditCategoryBottomSheet
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.InvalidCategoryException
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditCategoryViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val handle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditCategoryState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        initState()
    }

    fun onEvent(event: AddEditCategoryEvent) {
        when (event) {
            is AddEditCategoryEvent.OnInitType -> setType(event.type)
            is AddEditCategoryEvent.OnNameChange -> onNameChange(event.name)
            is AddEditCategoryEvent.OnIconChange -> onIconChange(event.icon)
            is AddEditCategoryEvent.OnColorChange -> onColorChange(event.color)
            is AddEditCategoryEvent.OnIsActiveChange -> onIsActiveChange()
            is AddEditCategoryEvent.OnBottomSheetChange -> onBottomSheetChange(event.type)
            is AddEditCategoryEvent.OnShowAlert -> onShowAlert(event.showAlert)
            is AddEditCategoryEvent.OnSubmitCategory -> onSaveCategory()
            is AddEditCategoryEvent.OnDeleteCategory -> onDeleteCategory()
        }
    }

    private fun initState() {
        handle.get<String?>(Screen.AddEditCategory.CATEGORY_ID)?.let { id ->
            if (id.isEmpty()) return
            viewModelScope.launch {
                categoryUseCases.getCategoryById(UUID.fromString(id))?.also {
                    _state.update { state ->
                        state.copy(
                            id = it.id,
                            name = it.name,
                            icon = it.icon,
                            color = it.color,
                            isActive = it.isActive
                        )
                    }
                }
            }
        }
    }

    private fun setType(type: CategoryType) {
        _state.update { it.copy(type = type) }
    }

    private fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
    }

    private fun onIconChange(icon: String) {
        _state.update { it.copy(icon = icon) }
    }

    private fun onColorChange(color: Long) {
        _state.update { it.copy(color = color) }
    }

    private fun onIsActiveChange() {
        val isActive = !_state.value.isActive
        _state.update { it.copy(isActive = isActive) }
        viewModelScope.launch {
            categoryUseCases.upsertCategory(state.value.category)
        }
    }

    private fun onBottomSheetChange(type: AddEditCategoryBottomSheet?) {
        _state.update { it.copy(bottomSheetType = type) }
    }

    private fun onShowAlert(showAlert: Boolean) {
        _state.update { it.copy(showAlert = showAlert) }
    }

    private fun onSaveCategory() {
        viewModelScope.launch {
            try {
                categoryUseCases.upsertCategory(state.value.category)
                _eventFlow.emit(UiEvent.SaveCategory)
            } catch (e: InvalidCategoryException) {
                _eventFlow.emit(UiEvent.ShowMessage(e.message))
            }
        }
    }

    private fun onDeleteCategory() {
        viewModelScope.launch {
            try {
                categoryUseCases.deleteCategory(state.value.category)
                _eventFlow.emit(UiEvent.DeleteCategory)
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowMessage(e.message ?: "Error deleting category"))
            }
        }
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String) : UiEvent()
        data object SaveCategory : UiEvent()
        data object DeleteCategory : UiEvent()
    }
}