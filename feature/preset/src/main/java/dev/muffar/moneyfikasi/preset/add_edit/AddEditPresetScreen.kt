package dev.muffar.moneyfikasi.preset.add_edit

import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.preset.add_edit.component.AddEditPresetButton
import dev.muffar.moneyfikasi.preset.add_edit.component.AddEditPresetForm
import dev.muffar.moneyfikasi.preset.add_edit.component.AddEditPresetTopBar
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditPresetScreen(
    state: AddEditPresetState,
    eventFlow: SharedFlow<AddEditPresetViewModel.UiEvent>,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onAddNewCategoryClick: () -> Unit,
    onWalletChange: (Wallet) -> Unit,
    onAddNewWalletClick: () -> Unit,
    onNoteChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = { SnackbarMessage(state = snackbarHostState) },
        topBar = { AddEditPresetTopBar(onBackClick) },
        bottomBar = { AddEditPresetButton(onSaveClick) }
    ) {
        AddEditPresetForm(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(16.dp),
            state = state,
            onNameChange = onNameChange,
            onAmountChange = onAmountChange,
            onCategoryChange = onCategoryChange,
            onAddNewCategoryClick = onAddNewCategoryClick,
            onWalletChange = onWalletChange,
            onAddNewWalletClick = onAddNewWalletClick,
            onNoteChange = onNoteChange
        )
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is AddEditPresetViewModel.UiEvent.SavePreset -> onBackClick()
                is AddEditPresetViewModel.UiEvent.ShowMessage -> snackbarHostState.showMessage(
                    it.message,
                    it.type
                )
            }
        }
    }
}
