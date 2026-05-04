package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.button.TopBarIconButton
import dev.muffar.moneyfikasi.resource.R

@Composable
fun TransactionDetailTopBar(
    onBackClick: () -> Unit,
    onDeleteClick: (Boolean) -> Unit,
    onEditClick: () -> Unit
) {
    CommonTopAppBar(
        title = stringResource(R.string.transaction),
        onBackClick = onBackClick,
        action = {
            TopBarIconButton(
                painter = painterResource(R.drawable.ic_delete),
                color = MaterialTheme.colorScheme.error,
                onClick = { onDeleteClick(true) }
            )

            TopBarIconButton(
                painter = painterResource(R.drawable.ic_edit),
                onClick = onEditClick
            )
        }
    )
}