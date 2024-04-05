package dev.muffar.moneyfikasi.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Category
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.settings.component.SettingsItem
import dev.muffar.moneyfikasi.settings.component.SettingsTopBar
import dev.muffar.moneyfikasi.resource.R

@Composable
fun SettingsScreen(
    modifier : Modifier = Modifier,
    onCategoriesClick : () -> Unit
) {
    Scaffold(
        topBar = {
            SettingsTopBar(modifier = Modifier.padding(16.dp))
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .padding(vertical = 8.dp)
        ) {
            SettingsItem(
                title = stringResource(R.string.categories),
                icon = Icons.TwoTone.Category,
                onClick = onCategoriesClick
            )
        }
    }
}