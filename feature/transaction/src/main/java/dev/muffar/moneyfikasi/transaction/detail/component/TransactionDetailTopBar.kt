package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.top_bar.TopBarButton
import dev.muffar.moneyfikasi.resource.R

@Composable
fun TransactionDetailTopBar(
    onBackClick: () -> Unit,
    onDeleteClick: (Boolean) -> Unit,
    onEditClick: () -> Unit
) {
    CommonTopAppBar(
        title = stringResource(R.string.label_transaction),
        onBackClick = onBackClick,
        action = {
            TopBarButton(
                imageVector = Icons.Rounded.Delete,
                color = MaterialTheme.colorScheme.error,
                onClick = { onDeleteClick(true) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            TopBarButton(
                imageVector = Icons.Rounded.Edit,
                onClick = onEditClick
            )
        }
    )
}