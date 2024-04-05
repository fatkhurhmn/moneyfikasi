package dev.muffar.moneyfikasi.category.add

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.CategoryType
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor() : ViewModel() {

    private val _state = mutableStateOf(AddCategoryState())
    val state : State<AddCategoryState> = _state

    fun onEvent(event: AddCategoryEvent) {
        when(event) {
            is AddCategoryEvent.OnInitType -> setType(event.type)
        }
    }

    private fun setType(type: CategoryType?) {
        _state.value = _state.value.copy(type = type)
    }
}