package dev.muffar.moneyfikasi.applock.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.RowRadioButton
import dev.muffar.moneyfikasi.domain.model.AppLockType
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AppLockTypeRadioGroup(
    selected: AppLockType,
    isBiometricAvailable: Boolean,
    onTypeChanged: (AppLockType) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.lock_type),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RowRadioButton(
                label = stringResource(R.string.pin),
                selected = selected == AppLockType.PIN,
                onClick = { onTypeChanged(AppLockType.PIN) }
            )
            if (isBiometricAvailable) {
                Spacer(modifier = Modifier.width(16.dp))
                RowRadioButton(
                    label = stringResource(R.string.biometric),
                    selected = selected == AppLockType.BIOMETRIC,
                    onClick = { onTypeChanged(AppLockType.BIOMETRIC) }
                )
            }
        }
    }
}
