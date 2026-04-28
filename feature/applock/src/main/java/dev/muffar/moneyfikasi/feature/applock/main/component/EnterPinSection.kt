package dev.muffar.moneyfikasi.feature.applock.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.CommonSwitch
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.resource.R

@Composable
fun EnterPinSection(
    isAppLockEnabled: Boolean,
    isBiometricEnabled: Boolean,
    isBiometricSupported: Boolean,
    onPinEnabled: (Boolean) -> Unit,
    onBiometricEnabled: (Boolean) -> Unit,
    onChangePinClick: () -> Unit
) {
    PrimaryCard {
        CommonSwitch(
            isEnabled = isAppLockEnabled,
            onEnabledChange = onPinEnabled,
            title = stringResource(R.string.app_lock),
            description = stringResource(R.string.app_lock_description)
        )

        if (isAppLockEnabled) {
            if (isBiometricSupported) {
                CommonSwitch(
                    isEnabled = isBiometricEnabled,
                    onEnabledChange = onBiometricEnabled,
                    title = stringResource(R.string.biometric),
                    description = stringResource(R.string.biometric_description)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onChangePinClick() }
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.reset_pin),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
