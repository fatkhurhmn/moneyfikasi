package dev.muffar.moneyfikasi.wallet.add_edit.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.wallet.add_edit.AddEditWalletEvent
import dev.muffar.moneyfikasi.wallet.add_edit.AddEditWalletScreen
import dev.muffar.moneyfikasi.wallet.add_edit.AddEditWalletViewModel
import java.util.UUID

fun NavGraphBuilder.addEditWalletNavigation(
    navigateBack: () -> Unit
){
    composable(Screen.AddEditWallet.route){
        val viewModel = hiltViewModel<AddEditWalletViewModel>()
        val state = viewModel.state.value
        val event = viewModel::onEvent

        AddEditWalletScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onNameChange = { name ->
                event(AddEditWalletEvent.OnNameChange(name))
            },
            onBalanceChange = { balance ->
                event(AddEditWalletEvent.OnBalanceChange(balance))
            },
            onIconChange = { icon ->
                event(AddEditWalletEvent.OnIconChange(icon))
            },
            onColorChange = { color ->
                event(AddEditWalletEvent.OnColorChange(color))
            },
            onIsActiveChange = { event(AddEditWalletEvent.OnIsActiveChange) },
            onShowBottomSheet = { sheetType ->
                event(AddEditWalletEvent.OnBottomSheetChange(sheetType))
            },
            onShowAlert = { showAlert ->
                event(AddEditWalletEvent.OnShowAlert(showAlert))
            },
            onSubmit = { event(AddEditWalletEvent.OnSubmitWallet) },
            onDelete = { event(AddEditWalletEvent.OnDeleteWallet) },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toAddEditWalletScreen(id: UUID? = null) {
    navigate(Screen.AddEditWallet.routeWithArg(id))
}