package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.button.bottom_bar.BottomBarButton
import dev.muffar.moneyfikasi.resource.R

@Composable
fun TransactionDetailSaveButton(
    onClick: () -> Unit
) {
    BottomBarButton(
        title = stringResource(R.string.action_save_to_gallery),
        onClick = onClick
    )
}