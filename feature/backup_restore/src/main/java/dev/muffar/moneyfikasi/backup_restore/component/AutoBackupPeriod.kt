package dev.muffar.moneyfikasi.backup_restore.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AutoBackupPeriod(
    period: TimePeriod,
    onPeriodSelected: (TimePeriod) -> Unit
) {

    var showPeriodMenu by remember { mutableStateOf(false) }
    val periods = listOf(
        stringResource(R.string.label_daily) to TimePeriod.DAILY,
        stringResource(R.string.label_weekly) to TimePeriod.WEEKLY,
        stringResource(R.string.label_monthly) to TimePeriod.MONTHLY
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showPeriodMenu = true }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.label_backup_period),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = periods.find { it.second == period }?.first ?: period.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        DropdownMenu(
            expanded = showPeriodMenu,
            onDismissRequest = { showPeriodMenu = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            periods.forEach { (label, value) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onPeriodSelected(value)
                        showPeriodMenu = false
                    }
                )
            }
        }
    }
}