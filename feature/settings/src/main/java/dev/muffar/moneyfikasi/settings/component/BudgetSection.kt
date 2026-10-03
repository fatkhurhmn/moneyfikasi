package dev.muffar.moneyfikasi.settings.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingItem
import dev.muffar.moneyfikasi.resource.R

@Composable
fun BudgetSection(
    budgetCutoffDay: Int,
    autoOpenPicker: Boolean = false,
    onBudgetCutoffChanged: (Int) -> Unit
) {
    var showPicker by remember(autoOpenPicker) { mutableStateOf(autoOpenPicker) }

    val subtitle = if (budgetCutoffDay == 1) {
        stringResource(R.string.label_monthly) + " (1 - Akhir bulan)"
    } else {
        "Tanggal $budgetCutoffDay - ${budgetCutoffDay - 1} bulan berikutnya"
    }

    PrimaryCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            SettingItem(
                title = stringResource(R.string.label_budget_period),
                subtitle = subtitle,
                icon = Icons.Rounded.CalendarToday,
                onClick = { showPicker = true }
            )
        }

        AnimatedVisibility(showPicker) {
            BudgetCutoffPickerSheet(
                selectedDay = budgetCutoffDay,
                onDaySelect = onBudgetCutoffChanged,
                onDismissRequest = { showPicker = false }
            )
        }
    }
}
