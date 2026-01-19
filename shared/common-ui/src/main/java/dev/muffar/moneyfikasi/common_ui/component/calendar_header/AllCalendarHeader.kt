package dev.muffar.moneyfikasi.common_ui.component.calendar_header

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AllCalendarHeader(
    onDateChange: () -> Unit,
) {


    CalendarHeader(
        title = stringResource(R.string.all),
        enableButton = false,
    )
}