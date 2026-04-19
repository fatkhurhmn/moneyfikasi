package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
    onPresetsClick: () -> Unit,
) {
    Column {
        DashboardLabel(
            label = stringResource(R.string.quick_transactions),
            moreText = stringResource(R.string.presets),
            onMoreClick = onPresetsClick
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (presets.isNotEmpty()) {
            PresetGrid(
                presets = presets,
                onPresetClick = onPresetClick
            )
        } else {
            EmptyPresetSection(onAddPresetClick = onAddPresetClick)
        }
    }
}

