package dev.muffar.moneyfikasi.common_ui.component.bottom_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.button.row.RowNegativePositiveButton
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletPickerSheet(
    selectedWallet: Wallet,
    walletOptions: List<Wallet>,
    onWalletSelect: (Wallet) -> Unit,
    onAddNewWalletClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val hideSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest()
            }
        }
    }

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        sheetGesturesEnabled = false
    ) {
        BottomSheetTitle(stringResource(R.string.label_select_wallet_hint))
        if (walletOptions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(walletOptions) { wallet ->
                    PickerOptionItem(
                        isSelected = selectedWallet == wallet,
                        icon = wallet.icon,
                        color = wallet.color,
                        title = wallet.name,
                        subtitle = wallet.balance.formatThousand(),
                        onClick = {
                            hideSheet()
                            onWalletSelect(wallet)
                        }
                    )
                }
            }
        } else {
            EmptyDataList(
                title = stringResource(id = R.string.empty_wallets_title),
                description = stringResource(id = R.string.empty_wallets_msg),
                modifier = Modifier.weight(1f)
            )
        }

        CommonHorizontalDivider()
        RowNegativePositiveButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            negativeText = stringResource(R.string.action_cancel),
            positiveText = stringResource(R.string.action_create),
            onNegativeClick = { hideSheet() },
            onPositiveClick = {
                hideSheet()
                onAddNewWalletClick()
            }
        )
    }
}
