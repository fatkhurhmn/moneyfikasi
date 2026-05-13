package dev.muffar.moneyfikasi.more.component

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
import dev.muffar.moneyfikasi.resource.R

@Composable
fun MoreTopBar(
    onSettingsClick: () -> Unit
) {
    CommonTopAppBar(
        title = stringResource(R.string.more_menu),
        containerColor = MaterialTheme.colorScheme.background,
        showBackButton = false,
        action = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings_outline),
                    contentDescription = stringResource(R.string.settings_menu),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}