package dev.muffar.moneyfikasi.transaction.detail.plus

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.ModifierExt.formModifier
import dev.muffar.moneyfikasi.common_ui.component.button.bottom_bar.BottomBarSaveButton
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.detail.plus.component.DebtPlusForm
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DebtPlusScreen(
    state: DebtPlusState,
    eventFlow: SharedFlow<DebtPlusViewModel.UiEvent>,
    onTabSelected: (Int) -> Unit,
    onAmountChange: (String) -> Unit,
    onWalletSelect: (Wallet) -> Unit,
    onAddNewWalletClick: () -> Unit,
    onDateSelect: (Long) -> Unit,
    onSubmit: () -> Unit,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.title_debt),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomBarSaveButton(onSubmit)
        },
        snackbarHost = { SnackbarMessage(snackbarHostState) },
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        DebtPlusForm(
            modifier = Modifier.formModifier(it, scrollState),
            state = state,
            onTabSelected = onTabSelected,
            onAmountChange = onAmountChange,
            onWalletSelect = onWalletSelect,
            onAddNewWalletClick = onAddNewWalletClick,
            onDateSelect = onDateSelect
        )
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is DebtPlusViewModel.UiEvent.SaveDebtPlus -> onBackClick()
                is DebtPlusViewModel.UiEvent.NavigateBack -> onBackClick()
                is DebtPlusViewModel.UiEvent.ShowMessage -> {
                    snackbarHostState.showMessage(
                        context.applicationContext.getString(it.messageResId),
                        it.type
                    )
                }
            }
        }
    }
}
