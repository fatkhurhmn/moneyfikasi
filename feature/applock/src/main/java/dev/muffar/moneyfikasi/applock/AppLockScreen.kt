package dev.muffar.moneyfikasi.applock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.applock.component.AppLockTypeRadioGroup
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.button.BottomBarButton
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.domain.model.AppLockType
import dev.muffar.moneyfikasi.resource.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLockScreen(
    state: AppLockState,
    onAppLockEnabledChanged: (Boolean) -> Unit,
    onAppLockTypeChanged: (AppLockType) -> Unit,
    onPinChanged: (String) -> Unit,
    onConfirmPinChanged: (String) -> Unit,
    onSaveAppLock: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.app_lock),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomBarButton(
                title = stringResource(R.string.save),
                onClick = onSaveAppLock
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                Spacer(modifier = Modifier.height(24.dp))
                AppLockTypeRadioGroup(
                    selected = state.appLockType,
                    isBiometricAvailable = state.isBiometricAvailable,
                    onTypeChanged = onAppLockTypeChanged
                )

                if (state.appLockType == AppLockType.PIN) {
                    Spacer(modifier = Modifier.height(24.dp))
                    CommonTextInput(
                        value = state.pin,
                        onValueChange = onPinChanged,
                        label = stringResource(R.string.pin),
                        placeholder = "****",
                        error = state.error,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CommonTextInput(
                        value = state.confirmPin,
                        onValueChange = onConfirmPinChanged,
                        label = stringResource(R.string.confirm_pin),
                        placeholder = "****",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            }
        }
    }
}
