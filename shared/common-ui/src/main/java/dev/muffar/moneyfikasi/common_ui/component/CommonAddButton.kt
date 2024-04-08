package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.resource.R

@Composable
fun CommonAddButton(
    modifier: Modifier = Modifier,
    contentDescription: String = stringResource(R.string.add),
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = contentDescription
        )
    }
}