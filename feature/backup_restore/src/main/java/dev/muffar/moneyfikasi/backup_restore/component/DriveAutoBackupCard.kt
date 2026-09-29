package dev.muffar.moneyfikasi.backup_restore.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingSwitchItem
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.resource.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Update

@Composable
fun DriveAutoBackupCard(
    isEnabled: Boolean,
    period: TimePeriod,
    onEnabledChange: (Boolean) -> Unit,
    onPeriodSelected: (TimePeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryCard(
        onClick = { onEnabledChange(!isEnabled) },
        modifier = modifier
    ) {
        Column {
            SettingSwitchItem(
                isEnabled = isEnabled,
                onEnabledChange = onEnabledChange,
                title = stringResource(R.string.msg_automatic_backup),
                subtitle = stringResource(R.string.msg_drive_auto_backup_description),
                icon = Icons.Rounded.Update
            )
            if (isEnabled) {
                CommonHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 64.dp),
                ) {
                    AutoBackupPeriod(
                        period = period,
                        onPeriodSelected = onPeriodSelected
                    )
                }
            }
        }
    }
}
