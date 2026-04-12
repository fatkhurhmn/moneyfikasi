package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.Preset
import dev.muffar.moneyfikasi.domain.model.TransactionType
import java.util.UUID
import kotlin.math.ceil

@Composable
fun PresetGrid(
    presets: List<Preset>,
    onPresetClick: (type: TransactionType, id: UUID) -> Unit
) {

    val itemWidth = 70.dp
    val spacing = 12.dp
    val horizontalPadding = 16.dp
    val aspectRatio = 1f

    val itemHeight = itemWidth / aspectRatio
    val gridHeight = (itemHeight * 2) + spacing

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val availableWidth = maxWidth - (horizontalPadding * 2)
        val itemsPerRow = remember(maxWidth) {
            ((availableWidth + spacing) / (itemWidth + spacing))
                .toInt()
                .coerceAtLeast(1)
        }
        val maxItemsInTwoRows = itemsPerRow * 2
        val useGrid = presets.size > maxItemsInTwoRows

        if (!useGrid) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
                presets.forEach { preset ->
                    QuickTransactionItem(
                        itemWidth = itemWidth,
                        aspectRatio = aspectRatio,
                        preset = preset,
                        onClick = { onPresetClick(preset.type, preset.id) }
                    )
                }
            }
        } else {
            LazyHorizontalGrid(
                rows = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(gridHeight),
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                items(
                    items = presets.toRowMajorOrder(rows = 2),
                    key = { it.id }
                ) { preset ->
                    QuickTransactionItem(
                        itemWidth = itemWidth,
                        aspectRatio = aspectRatio,
                        preset = preset,
                        onClick = { onPresetClick(preset.type, preset.id) }
                    )
                }
            }
        }
    }
}

fun <T> List<T>.toRowMajorOrder(rows: Int): List<T> {
    if (isEmpty()) return this

    val columns = ceil(size / rows.toFloat()).toInt()
    val result = MutableList<T?>(size) { null }

    for (row in 0 until rows) {
        for (col in 0 until columns) {
            val sourceIndex = col * rows + row
            val targetIndex = row * columns + col

            if (sourceIndex < size && targetIndex < size) {
                result[sourceIndex] = this[targetIndex]
            }
        }
    }

    return result.filterNotNull()
}