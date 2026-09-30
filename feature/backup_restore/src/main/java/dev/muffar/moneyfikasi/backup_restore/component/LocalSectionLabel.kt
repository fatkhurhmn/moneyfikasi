package dev.muffar.moneyfikasi.backup_restore.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.resource.R

@Composable
fun LocalSectionLabel(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.title_section_local),
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
    )
}
