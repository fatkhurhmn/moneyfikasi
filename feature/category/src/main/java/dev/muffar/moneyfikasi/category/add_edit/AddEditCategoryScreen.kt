package dev.muffar.moneyfikasi.category.add_edit

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.category.add_edit.component.AddEditCategoryButton
import dev.muffar.moneyfikasi.category.add_edit.component.AddEditCategoryForm
import dev.muffar.moneyfikasi.common_ui.component.CommonAlertDialog
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCategoryScreen(
    state: AddEditCategoryState,
    eventFlow: SharedFlow<AddEditCategoryViewModel.UiEvent>,
    onNameChange: (String) -> Unit,
    onIconSelect: (String) -> Unit,
    onColorSelect: (Long) -> Unit,
    onIsActiveChange: () -> Unit,
    onShowAlert: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onDelete: () -> Unit,
    onBackClick: () -> Unit,
) {
    val title = if (state.category.isIncome) {
        stringResource(R.string.income_category)
    } else {
        stringResource(R.string.expense_category)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = title,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            AddEditCategoryButton(
                isEdit = state.id != null,
                onSave = onSubmit,
                onDelete = { onShowAlert(true) }
            )
        },
        snackbarHost = { SnackbarMessage(snackbarHostState) }
    ) {
        AddEditCategoryForm(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(16.dp),
            state = state,
            onNameChange = onNameChange,
            onIconSelect = onIconSelect,
            onColorSelect = onColorSelect,
            onIsActiveChange = onIsActiveChange,
        )
    }

    if (state.showAlert) {
        CommonAlertDialog(
            title = stringResource(R.string.delete_category),
            message = stringResource(R.string.delete_category_confirmation),
            positiveText = stringResource(R.string.delete),
            negativeText = stringResource(R.string.cancel),
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
                is AddEditCategoryViewModel.UiEvent.SaveCategory -> onBackClick()
                is AddEditCategoryViewModel.UiEvent.DeleteCategory -> onBackClick()
                is AddEditCategoryViewModel.UiEvent.ShowMessage -> snackbarHostState.showMessage(
                    it.message,
                    it.type
                )
            }
        }
    }
}