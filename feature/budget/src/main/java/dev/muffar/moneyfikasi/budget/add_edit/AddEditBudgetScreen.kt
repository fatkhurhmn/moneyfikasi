package dev.muffar.moneyfikasi.budget.add_edit

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.budget.add_edit.component.AddEditBudgetForm
import dev.muffar.moneyfikasi.common_ui.component.button.bottom_bar.BottomBarAddEditButton
import dev.muffar.moneyfikasi.common_ui.component.dialog.CommonAlertDialog
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEditBudgetScreen(
    state: AddEditBudgetState,
    eventFlow: SharedFlow<AddEditBudgetViewModel.UiEvent>,
    onAmountChange: (String) -> Unit,
    onCategorySelect: (Category) -> Unit,
    onAddNewCategoryClick: () -> Unit,
    onShowAlert: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onDelete: () -> Unit,
    onBackClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.title_budget),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomBarAddEditButton(
                isEdit = state.id != null,
                onSave = onSubmit,
                onDelete = { onShowAlert(true) }
            )
        },
        snackbarHost = { SnackbarMessage(snackbarHostState) }
    ) {
        AddEditBudgetForm(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(16.dp),
            state = state,
            onAmountChange = onAmountChange,
            onCategorySelect = onCategorySelect,
            onAddNewCategoryClick = onAddNewCategoryClick
        )
    }

    if (state.showAlert) {
        CommonAlertDialog(
            title = stringResource(R.string.title_delete_budget),
            message = stringResource(R.string.msg_delete_budget_confirmation),
            positiveText = stringResource(R.string.action_delete),
            negativeText = stringResource(R.string.action_cancel),
            onDismiss = { onShowAlert(false) },
            onConfirm = {
                onDelete()
                onShowAlert(false)
            }
        )
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is AddEditBudgetViewModel.UiEvent.SaveBudget -> onBackClick()
                is AddEditBudgetViewModel.UiEvent.DeleteBudget -> onBackClick()
                is AddEditBudgetViewModel.UiEvent.ShowMessage -> {
                    snackbarHostState.showMessage(
                        context.applicationContext.getString(it.messageResId),
                        it.type
                    )
                }
            }
        }
    }
}
