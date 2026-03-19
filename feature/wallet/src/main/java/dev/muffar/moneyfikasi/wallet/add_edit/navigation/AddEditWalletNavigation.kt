package dev.muffar.moneyfikasi.wallet.add_edit.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent

        AddEditWalletScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onNameChange = { name ->
                event(AddEditWalletEvent.NameChanged(name))
            },
            onBalanceChange = { balance ->
                event(AddEditWalletEvent.BalanceChanged(balance))
            },
            onIconChange = { icon ->
                event(AddEditWalletEvent.IconChanged(icon))
            },
            onColorChange = { color ->
                event(AddEditWalletEvent.ColorChanged(color))
            },
            onWalletActive = { event(AddEditWalletEvent.WalletActivated) },
            onShowBottomSheet = { sheetType ->
                event(AddEditWalletEvent.BottomSheetChanged(sheetType))
            },
            onShowAlert = { showAlert ->
                event(AddEditWalletEvent.ShowDeleteAlert(showAlert))
            },
            onSubmit = { event(AddEditWalletEvent.SaveWallet) },
            onDelete = { event(AddEditWalletEvent.DeleteWallet) },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toAddEditWalletScreen(id: UUID? = null) {
    navigate(Screen.AddEditWallet.routeWithArg(id))
}