package dev.muffar.moneyfikasi.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingItem
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AboutSection(
    onAboutClick: () -> Unit,
) {
    Column {
        Text(
            text = stringResource(R.string.title_about),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        PrimaryCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            SettingItem(
                title = stringResource(R.string.title_about),
                subtitle = stringResource(R.string.msg_about_description),
                icon = Icons.Rounded.Info,
                onClick = onAboutClick
            )
        }
    }
}

