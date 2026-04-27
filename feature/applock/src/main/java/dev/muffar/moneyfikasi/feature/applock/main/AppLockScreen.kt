package dev.muffar.moneyfikasi.feature.applock.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLockScreen(
    state: AppLockState,
    eventFlow: SharedFlow<AppLockViewModel.UiEvent>,
    onAppLockEnabledChanged: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onNavigateToSetPin: () -> Unit
) {
    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is AppLockViewModel.UiEvent.NavigateToSetPin -> onNavigateToSetPin()
            }
        }
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.app_lock),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.enable_app_lock),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = state.isAppLockEnabled,
                    onCheckedChange = onAppLockEnabledChanged
                )
            }

            if (state.isAppLockEnabled) {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToSetPin() }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.change_pin),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
