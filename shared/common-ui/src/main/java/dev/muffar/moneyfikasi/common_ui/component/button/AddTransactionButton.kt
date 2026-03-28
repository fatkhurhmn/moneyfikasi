package dev.muffar.moneyfikasi.common_ui.component.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AddTransactionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(
            shape = MaterialTheme.shapes.medium,
            onClick = onClick,
            modifier = Modifier.size(50.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(id = R.string.create_transaction),
            )
        }
    }
}