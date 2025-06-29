package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
            painter = painterResource(id = R.drawable.ic_add),
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}