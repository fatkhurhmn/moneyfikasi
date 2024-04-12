package dev.muffar.moneyfikasi.transaction.add_edit.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerSheet(
    currentDate: Long,
    onDateSelect: (Long) -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDate
    )
    Column {
        DatePicker(
            state = datePickerState
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { onDateSelect(datePickerState.selectedDateMillis ?: 0) }) {
                Text(text = stringResource(R.string.select))
            }
        }
    }
}