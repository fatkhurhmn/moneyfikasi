package dev.muffar.moneyfikasi.transaction.transfer.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.button.BottomBarButton
import dev.muffar.moneyfikasi.resource.R

@Composable
fun TransferTransactionButton(
    onTransfer: () -> Unit,
) {
    BottomBarButton(
        title = stringResource(R.string.transfer),
        onClick = onTransfer
    )
}