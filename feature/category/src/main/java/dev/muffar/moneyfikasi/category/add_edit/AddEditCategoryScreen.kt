package dev.muffar.moneyfikasi.category.add_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.category.add_edit.component.AddEditCategoryBottomSheet
import dev.muffar.moneyfikasi.category.add_edit.component.AddEditCategoryButton
import dev.muffar.moneyfikasi.category.add_edit.component.AddEditCategoryForm
import dev.muffar.moneyfikasi.common_ui.component.CommonAlertDialog
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCategoryScreen(
    modifier: Modifier = Modifier,
    state: AddEditCategoryState,
    eventFlow: SharedFlow<AddEditCategoryViewModel.UiEvent>,
    onNameChange: (String) -> Unit,
    onIconChange: (String) -> Unit,
    onColorChange: (Long) -> Unit,
    onIsActiveChange: () -> Unit,
    onShowBottomSheet: (AddEditCategoryBottomSheet?) -> Unit,
    onShowAlert: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onDelete: () -> Unit,
    onBackClick: () -> Unit,
) {
    val title = if (state.type == CategoryType.INCOME) {
        stringResource(R.string.income_category)
    } else {
        stringResource(R.string.expense_category)
    }

    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is AddEditCategoryViewModel.UiEvent.SaveCategory -> onBackClick()
                is AddEditCategoryViewModel.UiEvent.DeleteCategory -> onBackClick()
                is AddEditCategoryViewModel.UiEvent.ShowMessage -> snackbarHostState.showSnackbar(it.message)
            }
        }
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = title,
                onBackClick = onBackClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
                .verticalScroll(scrollState)
                .imePadding()
                .padding(16.dp)
        ) {
            AddEditCategoryForm(
                id = state.id,
                name = state.name,
                icon = state.icon,
                color = state.color,
                isActive = state.isActive,
                onNameChange = onNameChange,
                onIconClick = {
                    onShowBottomSheet(AddEditCategoryBottomSheet.ICON)
                },
                onColorClick = {
                    onShowBottomSheet(AddEditCategoryBottomSheet.COLOR)
                },
                onIsActiveChange = onIsActiveChange,
            )

            Spacer(modifier = Modifier.height(24.dp))

            AddEditCategoryButton(
                isEdit = state.id != null,
                onSave = onSubmit,
                onDelete = { onShowAlert(true) }
            )
        }

        if (state.bottomSheetType != null) {
            ModalBottomSheet(
                onDismissRequest = { onShowBottomSheet(null) },
                sheetState = sheetState
            ) {
                AddEditCategoryBottomSheet(
                    type = state.bottomSheetType,
                    categoryType = state.type,
                    onIconSelect = { icon ->
                        onIconChange(icon)
                        onShowBottomSheet(null)
                    },
                    onColorSelect = { color ->
                        onColorChange(color)
                    },
                    onDismiss = { onShowBottomSheet(null) }
                )
            }
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
    }
}