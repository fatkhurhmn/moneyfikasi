package dev.muffar.moneyfikasi.more.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.top_bar.TopBarButton
import dev.muffar.moneyfikasi.resource.R

@Composable
fun MoreTopBar(
    onSettingsClick: () -> Unit
) {
    CommonTopAppBar(
        title = stringResource(R.string.more_menu),
        showBackButton = false,
        titleSize = 20.sp,
        action = {
            TopBarButton(
                imageVector = Icons.Rounded.Settings,
                onClick = onSettingsClick
            )
        }
    )
}