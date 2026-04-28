package dev.muffar.moneyfikasi.more.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R

@Composable
fun MoreTopBar() {
    CommonTopAppBar(
        title = stringResource(R.string.more_menu),
        showBackButton = false,
    )
}