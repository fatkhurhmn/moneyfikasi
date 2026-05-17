package dev.muffar.moneyfikasi.feature.applock.main.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingItem
import dev.muffar.moneyfikasi.resource.R

@Composable
fun ResetPinSection(onChangePinClick: () -> Unit) {
    PrimaryCard {
        SettingItem(
            title = stringResource(R.string.reset_pin),
            subtitle = stringResource(R.string.reset_pin_description),
            icon = Icons.Rounded.Refresh,
            onClick = onChangePinClick
        )
    }
}
