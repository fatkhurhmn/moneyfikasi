package dev.muffar.moneyfikasi.settings.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingItem
import dev.muffar.moneyfikasi.resource.R

@Composable
fun SecuritySection(
    onAppLockClick: () -> Unit,
) {
    PrimaryCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        SettingItem(
            title = stringResource(R.string.label_app_lock),
            subtitle = stringResource(R.string.msg_app_lock_description),
            icon = Icons.Rounded.Security,
            onClick = onAppLockClick
        )
    }
}
