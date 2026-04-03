package dev.muffar.moneyfikasi.preset.list.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
            painter = painterResource(id = R.drawable.ic_empty_transactions),
            title = "No Presets Yet",
            description = "Start adding one to track your finances."
        )
    }
}
