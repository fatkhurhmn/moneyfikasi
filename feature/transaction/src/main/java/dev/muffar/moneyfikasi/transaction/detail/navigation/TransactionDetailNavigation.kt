package dev.muffar.moneyfikasi.transaction.detail.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.transaction.detail.TransactionDetailEvent
import dev.muffar.moneyfikasi.transaction.detail.TransactionDetailScreen
import dev.muffar.moneyfikasi.transaction.detail.TransactionDetailViewModel
import java.util.UUID

fun NavGraphBuilder.transactionDetailNavigation(
    onNavigateToEditTransaction: (TransactionType, UUID) -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable(
        route = Screen.TransactionDetail.route
    ) {
        val viewModel = hiltViewModel<TransactionDetailViewModel>()
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent
        val eventFlow = viewModel.eventFlow

        LaunchedEffect(Unit) {
            event(TransactionDetailEvent.OnInitData)
        }

        TransactionDetailScreen(
            state = state,
            eventFlow = eventFlow,
            onDelete = { event(TransactionDetailEvent.OnDeleteTransaction) },
            onShowAlert = { event(TransactionDetailEvent.OnShowAlert(it)) },
            onEdit = onNavigateToEditTransaction,
            onBackClick = onNavigateBack,
            onSaveClick = { event(TransactionDetailEvent.OnSaveToGallery(it)) },
        )
    }
}

fun NavController.toTransactionDetail(id: UUID) {
    navigate(Screen.TransactionDetail.routeWithArg(id))
}