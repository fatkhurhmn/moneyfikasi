package dev.muffar.moneyfikasi.preset.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.icon.BoxedIcon
import dev.muffar.moneyfikasi.domain.model.Preset
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import java.util.UUID

@Composable
fun PresetItem(
    preset: Preset,
    onClick: (UUID) -> Unit,
) {
    PrimaryCard(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick(preset.id) }
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BoxedIcon(
                icon = preset.category?.icon,
                color = preset.category?.color,
                containerSize = 44.dp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = preset.name,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (preset.description?.isNotEmpty() == true) {
                    Text(
                        text = preset.description!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                horizontalAlignment = Alignment.End
            ) {
                val wallet = preset.wallet
                if (wallet != null) {
                    Text(
                        text = wallet.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color(wallet.color)
                    )
                }
                Text(
                    text = preset.amount?.formatThousand() ?: "0",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}