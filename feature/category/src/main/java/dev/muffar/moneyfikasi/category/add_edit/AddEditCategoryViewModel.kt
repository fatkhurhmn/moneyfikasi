package dev.muffar.moneyfikasi.category.add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.model.CategoryType
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
            is AddEditCategoryEvent.InitType -> setType(event.type)
            is AddEditCategoryEvent.NameChanged -> onNameChange(event.name)
            is AddEditCategoryEvent.IconChanged -> onIconChange(event.icon)
            is AddEditCategoryEvent.ColorChanged -> onColorChange(event.color)
            is AddEditCategoryEvent.ActivationEnabled -> onActivationEnable()
            is AddEditCategoryEvent.ShowDeleteAlert -> onShowDeleteAlert(event.showAlert)
            is AddEditCategoryEvent.SaveCategory -> onSaveCategory()
            is AddEditCategoryEvent.DeleteCategory -> onDeleteCategory()
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

    private fun onActivationEnable() {
        val isActive = !_state.value.isActive
        _state.update { it.copy(isActive = isActive) }
        viewModelScope.launch {
            categoryUseCases.upsertCategory(state.value.category)
        }
    }

    private fun onShowDeleteAlert(showAlert: Boolean) {
        _state.update { it.copy(showAlert = showAlert) }
    }

    private fun onSaveCategory() {
        viewModelScope.launch {
            try {
                categoryUseCases.upsertCategory(state.value.category)
                _eventFlow.emit(UiEvent.SaveCategory)
            } catch (e: Exception) {
                e.printStackTrace()
                _eventFlow.emit(
                    UiEvent.ShowMessage(
                        "Failed to save category",
                        SnackbarType.ERROR
                    )
                )
            }
        }
    }

    private fun onDeleteCategory() {
        viewModelScope.launch {
            try {
                categoryUseCases.deleteCategory(state.value.category)
                _eventFlow.emit(UiEvent.DeleteCategory)
            } catch (e: Exception) {
                e.printStackTrace()
                _eventFlow.emit(
                    UiEvent.ShowMessage(
                        "Failed to delete category",
                        SnackbarType.ERROR
                    )
                )
            }
        }
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String, val type: SnackbarType) : UiEvent()
        data object SaveCategory : UiEvent()
        data object DeleteCategory : UiEvent()
    }
}