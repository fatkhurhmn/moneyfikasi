package dev.muffar.moneyfikasi.settings.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R

@Composable
fun SettingsTopBar() {
    CommonTopAppBar(
        title = stringResource(R.string.settings_menu),
        showBackButton = false,
    )
}