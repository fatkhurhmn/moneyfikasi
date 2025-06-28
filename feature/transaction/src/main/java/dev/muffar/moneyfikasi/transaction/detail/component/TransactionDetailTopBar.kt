package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun TransactionDetailTopBar(
    transaction: Transaction?,
    onBackClick: () -> Unit,
    onDeleteClick: (Boolean) -> Unit,
    onEditClick: (type: TransactionType, id: UUID) -> Unit
) {
    CommonTopAppBar(
        title = stringResource(R.string.transaction),
        onBackClick = onBackClick,
        action = {
            IconButton(onClick = { onDeleteClick(true) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = stringResource(R.string.delete),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(26.dp)
                )
            }
            IconButton(
                onClick = {
                    transaction?.let {
                        onEditClick(it.type, it.id)
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = stringResource(R.string.edit),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    )
}