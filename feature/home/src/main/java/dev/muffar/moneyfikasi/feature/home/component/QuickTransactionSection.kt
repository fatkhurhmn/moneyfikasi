package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.Preset
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun QuickTransactionSection(
    presets: List<Preset>,
    onPresetClick: (TransactionType, UUID) -> Unit,
    onAddPresetClick: () -> Unit,
) {
    if (presets.isEmpty()) return

    Column {
        DashboardLabel(
            label = stringResource(R.string.quick_transaction),
            showSeeAll = false
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (presets.isNotEmpty()) {
            LazyHorizontalGrid(
                rows = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(presets, key = { it.id }) { preset ->
                    QuickTransactionItem(
                        preset = preset,
                        onClick = { onPresetClick(preset.type, preset.id) }
                    )
                }
            }
        } else {
            EmptyPresetSection(onAddPresetClick = onAddPresetClick)
        }
    }
}

