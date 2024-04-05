package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.resource.R

@Composable
fun CommonAddButton(
    modifier: Modifier = Modifier,
    contentDescription: String = stringResource(R.string.add),
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier.scale(1.2f)
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = contentDescription
        )
    }
}