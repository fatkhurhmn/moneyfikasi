package dev.muffar.moneyfikasi.preset.add_edit

import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import dev.muffar.moneyfikasi.common_ui.component.ModifierExt.formModifier
import dev.muffar.moneyfikasi.common_ui.component.button.bottom_bar.BottomBarAddEditButton
import dev.muffar.moneyfikasi.common_ui.component.dialog.CommonAlertDialog
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.preset.add_edit.component.AddEditPresetForm
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditPresetScreen(
    state: AddEditPresetState,
    eventFlow: SharedFlow<AddEditPresetViewModel.UiEvent>,
    onTypeChange: (TransactionType) -> Unit,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategoryChange: (Category?) -> Unit,
    onAddNewCategoryClick: () -> Unit,
    onWalletChange: (Wallet?) -> Unit,
    onAddNewWalletClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onShowDeleteAlert: (Boolean) -> Unit,
    onBackClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarMessage(state = snackbarHostState) },
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.title_preset),
                onBackClick = onBackClick,
            )
        },
        bottomBar = {
            BottomBarAddEditButton(
                isEdit = state.id != null,
                onSave = onSaveClick,
                onDelete = { onShowDeleteAlert(true) }
            )
        }
    ) {
        AddEditPresetForm(
            modifier = Modifier.formModifier(it, scrollState),
            state = state,
            onTypeChange = onTypeChange,
            onNameChange = onNameChange,
            onAmountChange = onAmountChange,
            onCategoryChange = onCategoryChange,
            onAddNewCategoryClick = onAddNewCategoryClick,
            onWalletChange = onWalletChange,
            onAddNewWalletClick = onAddNewWalletClick
        )
    }

    if (state.showAlert) {
        CommonAlertDialog(
            title = stringResource(R.string.title_delete_preset),
            message = stringResource(R.string.msg_delete_preset_confirmation),
            onConfirm = {
                onDeleteClick()
                onShowDeleteAlert(false)
            },
            onDismiss = { onShowDeleteAlert(false) },
            positiveText = stringResource(R.string.action_delete),
            negativeText = stringResource(R.string.action_cancel)
        )
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is AddEditPresetViewModel.UiEvent.SavePreset -> onBackClick()
                is AddEditPresetViewModel.UiEvent.DeletePreset -> onBackClick()
                is AddEditPresetViewModel.UiEvent.ShowMessage -> snackbarHostState.showMessage(
                    context.applicationContext.getString(it.messageResId),
                    it.type
                )
            }
        }
    }
}
