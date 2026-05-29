package dev.muffar.moneyfikasi.search.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R

@Composable
fun SearchTopBar() {
    CommonTopAppBar(
        title = stringResource(R.string.menu_search),
        showBackButton = false,
    )
}