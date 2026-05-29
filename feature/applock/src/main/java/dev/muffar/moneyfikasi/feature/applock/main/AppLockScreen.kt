package dev.muffar.moneyfikasi.feature.applock.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.EnterPinType
import dev.muffar.moneyfikasi.feature.applock.main.component.EnterPinSection
import dev.muffar.moneyfikasi.feature.applock.main.component.ResetPinSection
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLockScreen(
    state: AppLockState,
    eventFlow: SharedFlow<AppLockViewModel.UiEvent>,
    onAppLockEnabledChanged: (Boolean) -> Unit,
    onBiometricEnabledChanged: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onNavigateToEnterPin: (EnterPinType) -> Unit
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.label_app_lock),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            EnterPinSection(
                isAppLockEnabled = state.isAppLockEnabled,
                isBiometricEnabled = state.isBiometricEnabled,
                isBiometricSupported = state.isBiometricSupported,
                onPinEnabled = onAppLockEnabledChanged,
                onBiometricEnabled = onBiometricEnabledChanged,
            )
            if (state.isAppLockEnabled) {
                Spacer(modifier = Modifier.height(16.dp))
                ResetPinSection { onNavigateToEnterPin(EnterPinType.RESET_PIN) }
            }
        }
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is AppLockViewModel.UiEvent.NavigateToEnterPin -> onNavigateToEnterPin(it.type)
            }
        }
    }
}
