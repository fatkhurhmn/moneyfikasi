package dev.muffar.moneyfikasi.feature.applock.enter_pin

import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.pin_input.NumberPad
import dev.muffar.moneyfikasi.common_ui.component.pin_input.PinDots
import dev.muffar.moneyfikasi.common_ui.component.pin_input.PinHeader
import dev.muffar.moneyfikasi.domain.model.EnterPinStep
import dev.muffar.moneyfikasi.domain.model.EnterPinType
import dev.muffar.moneyfikasi.feature.applock.enter_pin.component.EnterPinTopAppBar
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterPinScreen(
    state: EnterPinState,
    eventFlow: SharedFlow<EnterPinViewModel.UiEvent>,
    onPinChanged: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onEnterPinSuccess: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val executor = remember { ContextCompat.getMainExecutor(context) }

    val biometricPrompt = remember {
        BiometricPrompt(
            context as FragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onEnterPinSuccess()
                }
            }
        )
    }

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(stringResource(R.string.biometric_unlock))
        .setSubtitle(stringResource(R.string.biometric_reason))
        .setNegativeButtonText(stringResource(R.string.use_pin))
        .build()

    LaunchedEffect(state.isBiometricEnabled) {
        if (state.type == EnterPinType.ENTER_PIN && state.isBiometricEnabled) {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    Scaffold(
        topBar = {
            EnterPinTopAppBar(
                type = state.type,
                onBackClick = onNavigateBack
            )
        },
        snackbarHost = { SnackbarMessage(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val title = when (state.step) {
                EnterPinStep.ENTER_PIN -> stringResource(R.string.enter_your_PIN)
                EnterPinStep.CONFIRM_PIN -> stringResource(R.string.confirm_your_PIN)
                EnterPinStep.VERIFY_CURRENT_PIN -> stringResource(R.string.enter_your_current_PIN)
                EnterPinStep.ENTER_NEW_PIN -> stringResource(R.string.enter_your_new_PIN)
                EnterPinStep.CONFIRM_NEW_PIN -> stringResource(R.string.confirm_your_PIN)
            }

            PinHeader(title = title, errorMessage = state.errorMessage)

            PinDots(filledCount = state.currentInput.length)

            NumberPad(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onDigit = { digit ->
                    if (state.currentInput.length < 4) {
                        onPinChanged(state.currentInput + digit)
                    }
                },
                onBackspace = {
                    if (state.currentInput.isNotEmpty()) {
                        onPinChanged(state.currentInput.dropLast(1))
                    }
                }
            )
        }
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is EnterPinViewModel.UiEvent.SavePin -> {
                    if (state.type == EnterPinType.ENTER_PIN) {
                        onEnterPinSuccess()
                    } else {
                        onNavigateBack()
                    }
                }

                is EnterPinViewModel.UiEvent.NavigateBack -> onNavigateBack()
            }
        }
    }
}
