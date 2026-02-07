package dev.muffar.moneyfikasi.transaction.transfer.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.button.CommonButton
import dev.muffar.moneyfikasi.common_ui.component.keyboardAsState
import dev.muffar.moneyfikasi.resource.R

@Composable
fun TransferTransactionButton(
    onTransfer: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val isKeyboardVisible by keyboardAsState()

    Column(
        modifier = Modifier.imePadding()
    ) {
        CommonHorizontalDivider()
        CommonButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            text = stringResource(R.string.transfer),
            onClick = {
                onTransfer()
                if (isKeyboardVisible) {
                    keyboardController?.hide()
                }
            }
        )
    }
}