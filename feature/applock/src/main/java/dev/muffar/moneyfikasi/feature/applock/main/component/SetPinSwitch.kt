package dev.muffar.moneyfikasi.feature.applock.main.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.button.CommonSwitch
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.feature.applock.main.AppLockState
import dev.muffar.moneyfikasi.resource.R

@Composable
fun SetPinSwitch(
    state: AppLockState,
    onAppLockEnabledChanged: (Boolean) -> Unit
) {
    PrimaryCard {
        CommonSwitch(
            isEnabled = state.isAppLockEnabled,
            onEnabledChange = onAppLockEnabledChanged,
            title = stringResource(R.string.set_pin),
            description = stringResource(R.string.app_lock_description)
        )
    }
}