package dev.muffar.moneyfikasi.feature.applock.enter_pin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.pin_input.NumberPad
import dev.muffar.moneyfikasi.common_ui.component.pin_input.PinDots
import dev.muffar.moneyfikasi.common_ui.component.pin_input.PinHeader
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterPinScreen(
    state: EnterPinState,
    eventFlow: SharedFlow<EnterPinViewModel.UiEvent>,
    onPinChanged: (String) -> Unit,
    onBackToEnterPin: () -> Unit,
    onCancel: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error.message) {
        if (state.error.message?.isNotEmpty() == true) {
            snackbarHostState.showSnackbar(state.error.message ?: "")
        }
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is EnterPinViewModel.UiEvent.SavePin -> onNavigateBack()
                is EnterPinViewModel.UiEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.set_pin),
                onBackClick = {
                    if (state.step == EnterPinStep.CONFIRM_PIN) {
                        onBackToEnterPin()
                    } else {
                        onCancel()
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val title = if (state.step == EnterPinStep.ENTER_PIN) {
                stringResource(R.string.enter_pin)
            } else {
                stringResource(R.string.confirm_pin)
            }

            val subtitle = if (state.step == EnterPinStep.ENTER_PIN) {
                stringResource(R.string.enter_4_digit_pin)
            } else {
                stringResource(R.string.re_enter_pin_to_confirm)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                PinHeader(
                    title = title,
                    subtitle = subtitle
                )

                Spacer(Modifier.height(48.dp))

                PinDots(filledCount = state.currentPin.length)
            }

            NumberPad(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onDigit = { digit ->
                    if (state.currentPin.length < 4) {
                        onPinChanged(state.currentPin + digit)
                    }
                },
                onBackspace = {
                    if (state.currentPin.isNotEmpty()) {
                        onPinChanged(state.currentPin.dropLast(1))
                    }
                }
            )
        }
    }
}
