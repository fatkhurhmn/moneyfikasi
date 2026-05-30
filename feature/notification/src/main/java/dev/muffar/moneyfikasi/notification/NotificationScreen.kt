package dev.muffar.moneyfikasi.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.notification.component.NotificationSection
import dev.muffar.moneyfikasi.resource.R

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    state: NotificationState,
    onEvent: (NotificationEvent) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.label_notification_section),
                onBackClick = onBackClick
            )
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            NotificationSection(
                state = state,
                onEvent = onEvent
            )
        }
    }
}
