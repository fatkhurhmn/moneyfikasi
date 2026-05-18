package dev.muffar.moneyfikasi.budget.add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.usecase.budget.BudgetUseCases
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.utils.constants.ValidationConst
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.StringExt.clearThousandFormat
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditBudgetViewModel @Inject constructor(
    private val budgetUseCases: BudgetUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val handle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditBudgetState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        initState()
        loadCategories()
        loadBudgets()
    }

    fun onEvent(event: AddEditBudgetEvent) {
        when (event) {
            is AddEditBudgetEvent.AmountChanged -> onAmountChange(event.amount)
            is AddEditBudgetEvent.CategoryChanged -> onCategorySelect(event.category)
            is AddEditBudgetEvent.SaveBudget -> onSaveBudget()
            is AddEditBudgetEvent.DeleteBudget -> onDeleteBudget()
            is AddEditBudgetEvent.ShowDeleteAlert -> onShowDeleteAlert(event.show)
        }
    }

    private fun initState() {
        handle.get<String>(Screen.AddEditBudget.BUDGET_ID)?.let { budgetId ->
            if (budgetId.isNotEmpty()) {
                viewModelScope.launch {
                    budgetUseCases.getBudgetById(UUID.fromString(budgetId))?.let { budget ->
                        _state.update {
                            it.copy(
                                id = budget.id,
                                amount = budget.amount.formatThousand(),
                                category = budget.category
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadBudgets() {
        viewModelScope.launch {
            budgetUseCases.getAllBudgets().collectLatest { budgets ->
                _state.update { it.copy(budgets = budgets) }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryUseCases.getCategoryByType(CategoryType.EXPENSE, false)
                .collectLatest { categories ->
                    _state.update { it.copy(categoryOptions = categories) }
                }
        }
    }

    private fun onAmountChange(amount: String) {
        if (amount.length > ValidationConst.MAX_AMOUNT_LENGTH) return
        _state.update { it.copy(amount = amount) }
        updateAmountError()
    }

    private fun updateAmountError() {
        val amount = _state.value.amount.clearThousandFormat().toDoubleOrNull() ?: 0.0
        val error = if (amount <= 0) "Amount must be greater than 0" else null
        _state.update { it.copy(amountError = ErrorMessage(error)) }
    }

    private fun onCategorySelect(category: Category?) {
        _state.update { it.copy(category = category ?: Category()) }
        updateCategoryError()
    }

    private fun updateCategoryError() {
        val currentCategory = _state.value.category
        val error = when {
            currentCategory.name.isEmpty() -> "Category must be selected"
            _state.value.id == null && currentCategory.id in _state.value.budgets.map { it.category.id } -> "This category already has a budget"
            else -> null
        }
        _state.update { it.copy(categoryError = ErrorMessage(error)) }
    }

    private fun onShowDeleteAlert(show: Boolean) {
        _state.update { it.copy(showAlert = show) }
    }

    private fun onSaveBudget() {
        if (!isFormValid()) return
        val state = _state.value
        viewModelScope.launch {
            try {
                budgetUseCases.upsertBudget(state.budget)
                _eventFlow.emit(UiEvent.SaveBudget)
            } catch (e: Exception) {
                e.printStackTrace()
                _eventFlow.emit(
                    UiEvent.ShowMessage(
                        "Failed to save budget. Each category can only have one budget.",
                        SnackbarType.ERROR
                    )
                )
            }
        }
    }

    private fun onDeleteBudget() {
        val state = _state.value
        if (state.id == null) return
        viewModelScope.launch {
            try {
                budgetUseCases.deleteBudget(state.budget)
                _eventFlow.emit(UiEvent.DeleteBudget)
            } catch (e: Exception) {
                e.printStackTrace()
                _eventFlow.emit(UiEvent.ShowMessage("Failed to delete budget", SnackbarType.ERROR))
            }
        }
    }

    private fun isFormValid(): Boolean {
        updateAmountError()
        updateCategoryError()
        val state = _state.value
        return state.amountError.isNull && state.categoryError.isNull
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String, val type: SnackbarType) : UiEvent()
        data object SaveBudget : UiEvent()
        data object DeleteBudget : UiEvent()
    }
}
