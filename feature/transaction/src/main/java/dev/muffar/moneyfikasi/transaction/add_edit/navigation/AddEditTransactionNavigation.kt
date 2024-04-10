package dev.muffar.moneyfikasi.transaction.add_edit.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.transaction.add_edit.AddEditTransactionEvent
import dev.muffar.moneyfikasi.transaction.add_edit.AddEditTransactionScreen
import dev.muffar.moneyfikasi.transaction.add_edit.AddEditTransactionViewModel
import java.util.UUID

fun NavGraphBuilder.addEditTransactionNavigation(
    onNavigateBack : () -> Unit
) {
    composable(Screen.AddEditTransaction.route) {
        val viewModel = hiltViewModel<AddEditTransactionViewModel>()
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent

        val type = it.arguments?.getString(Screen.AddEditTransaction.TYPE)?.let { value ->
            TransactionType.fromString(value)
        }

        LaunchedEffect(Unit) {
            event(AddEditTransactionEvent.OnInitType(type ?: TransactionType.EXPENSE))
        }

        AddEditTransactionScreen(
            modifier = Modifier,
            state = state,
            onBackClick = onNavigateBack
        )
    }
}

fun NavController.toAddEditTransactionScreen(type: TransactionType, id: UUID? = null) {
    navigate(Screen.AddEditTransaction.routeWithArg(type, id))
}