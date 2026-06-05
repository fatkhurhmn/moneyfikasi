package dev.muffar.moneyfikasi.category.add_edit

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.category.add_edit.component.AddEditCategoryForm
import dev.muffar.moneyfikasi.common_ui.component.ModifierExt.formModifier
import dev.muffar.moneyfikasi.common_ui.component.button.bottom_bar.BottomBarAddEditButton
import dev.muffar.moneyfikasi.common_ui.component.dialog.CommonAlertDialog
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCategoryScreen(
    state: AddEditCategoryState,
    eventFlow: SharedFlow<AddEditCategoryViewModel.UiEvent>,
    onTypeChange: (CategoryType) -> Unit,
    onNameChange: (String) -> Unit,
    onIconSelect: (String) -> Unit,
    onColorSelect: (Long) -> Unit,
    onCategoryActive: () -> Unit,
    onShowAlert: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onDelete: () -> Unit,
    onBackClick: () -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.label_category),
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
        snackbarHost = { SnackbarMessage(snackbarHostState) },
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        AddEditCategoryForm(
            modifier = Modifier.formModifier(it, scrollState),
            state = state,
            onTypeChange = onTypeChange,
            onNameChange = onNameChange,
            onIconSelect = onIconSelect,
            onColorSelect = onColorSelect,
            onCategoryActive = onCategoryActive,
        )
    }

    if (state.showAlert) {
        CommonAlertDialog(
            title = stringResource(R.string.title_delete_category),
            message = stringResource(R.string.msg_delete_category_confirmation),
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
                is AddEditCategoryViewModel.UiEvent.SaveCategory -> onBackClick()
                is AddEditCategoryViewModel.UiEvent.DeleteCategory -> onBackClick()
                is AddEditCategoryViewModel.UiEvent.ShowMessage -> snackbarHostState.showMessage(
                    context.applicationContext.getString(it.messageResId),
                    it.type
                )
            }
        }
    }
}
