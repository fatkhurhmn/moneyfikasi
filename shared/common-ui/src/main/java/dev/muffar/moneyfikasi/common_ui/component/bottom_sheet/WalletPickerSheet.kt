package dev.muffar.moneyfikasi.common_ui.component.bottom_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.button.DoubleOutlinedButton
import dev.muffar.moneyfikasi.common_ui.component.icon.BoxedIcon
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletPickerSheet(
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
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        sheetGesturesEnabled = false
    ) {
        BottomSheetTitle(stringResource(R.string.select_wallet))
        if (walletOptions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(walletOptions) { wallet ->
                    WalletOptionItem(
                        wallet = wallet,
                        onClick = {
                            hideSheet()
                            onWalletSelect(wallet)
                        }
                    )
                }
            }
        } else {
            EmptyDataList(
                painter = painterResource(id = R.drawable.ic_empty_wallet),
                title = stringResource(id = R.string.no_wallets),
                description = stringResource(id = R.string.no_wallets_message),
                modifier = Modifier.weight(1f)
            )
        }

        CommonHorizontalDivider()
        DoubleOutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            leftText = stringResource(R.string.cancel),
            rightText = stringResource(R.string.create),
            onLeftClick = { hideSheet() },
            onRightClick = {
                hideSheet()
                onAddNewWalletClick()
            }
        )
    }
}

@Composable
private fun WalletOptionItem(
    wallet: Wallet,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxedIcon(
            icon = wallet.icon,
            color = wallet.color
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = wallet.name, color = MaterialTheme.colorScheme.onSurface)
            Text(
                text = wallet.balance.formatThousand(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}