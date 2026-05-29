package dev.muffar.moneyfikasi.preset.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.domain.model.Preset
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun PresetsContent(
    modifier: Modifier = Modifier,
    presets: List<Preset>,
    onClick: (UUID) -> Unit
) {
    if (presets.isNotEmpty()) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(presets, key = { it.id }) { preset ->
                PresetItem(
                    preset = preset,
                    onClick = onClick
                )
            }
        }
    } else {
        EmptyDataList(
            title = stringResource(R.string.empty_presets_title),
            description = stringResource(R.string.empty_presets_msg)
        )
    }
}
