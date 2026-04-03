package dev.muffar.moneyfikasi.preset.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.button.CommonAddButton
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PresetListScreen(
    state: PresetListState,
    onAddPresetClick: () -> Unit,
    onPresetClick: (UUID) -> Unit,
    onBackClick: () -> Unit,
) {

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.preset),
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            CommonAddButton(
                onClick = onAddPresetClick
            )
        }
    ) {
        // Preset list content will be added here
    }
}
