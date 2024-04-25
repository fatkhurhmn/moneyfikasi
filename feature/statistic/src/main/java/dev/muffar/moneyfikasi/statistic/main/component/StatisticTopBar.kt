package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import dev.muffar.moneyfikasi.resource.R

@Composable
fun StatisticTopBar(
    modifier: Modifier = Modifier,
    onFilterClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.statistic_menu),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
        )
        IconButton(onClick = onFilterClick) {
            Icon(imageVector = Icons.Rounded.FilterList, contentDescription = null)
        }
    }
}