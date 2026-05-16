package dev.muffar.moneyfikasi.feature.applock.main.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard

@Composable
fun EnterPinSection(
    isAppLockEnabled: Boolean,
    isBiometricEnabled: Boolean,
    isBiometricSupported: Boolean,
    onPinEnabled: (Boolean) -> Unit,
    onBiometricEnabled: (Boolean) -> Unit,
) {
    PrimaryCard {
        AppLockSwitch(
            isAppLockEnabled = isAppLockEnabled,
            onPinEnabled = onPinEnabled
        )

        if (isBiometricSupported && isAppLockEnabled) {
            CommonHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            BiometricSwitch(
                isBiometricEnabled = isBiometricEnabled,
                onBiometricEnabled = onBiometricEnabled
            )
        }
    }
}
