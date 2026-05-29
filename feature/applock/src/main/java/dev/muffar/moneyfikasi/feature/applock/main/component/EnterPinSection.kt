package dev.muffar.moneyfikasi.feature.applock.main.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingSwitchItem
import dev.muffar.moneyfikasi.resource.R

@Composable
fun EnterPinSection(
    isAppLockEnabled: Boolean,
    isBiometricEnabled: Boolean,
    isBiometricSupported: Boolean,
    onPinEnabled: (Boolean) -> Unit,
    onBiometricEnabled: (Boolean) -> Unit,
) {
    PrimaryCard {
        Column {
            SettingSwitchItem(
                isEnabled = isAppLockEnabled,
                onEnabledChange = onPinEnabled,
                title = stringResource(R.string.label_app_lock),
                subtitle = stringResource(R.string.msg_app_lock_description),
                icon = Icons.Rounded.Lock
            )

            if (isBiometricSupported && isAppLockEnabled) {
                CommonHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingSwitchItem(
                    isEnabled = isBiometricEnabled,
                    onEnabledChange = onBiometricEnabled,
                    title = stringResource(R.string.label_biometric),
                    subtitle = stringResource(R.string.msg_biometric_description),
                    icon = Icons.Rounded.Fingerprint
                )
            }
        }
    }
}
