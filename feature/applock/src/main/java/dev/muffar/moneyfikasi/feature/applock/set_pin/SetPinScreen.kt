package dev.muffar.moneyfikasi.feature.applock.set_pin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.NumberPad
import dev.muffar.moneyfikasi.common_ui.component.PinDots
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetPinScreen(
    state: SetPinState,
    eventFlow: SharedFlow<SetPinViewModel.UiEvent>,
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
                is SetPinViewModel.UiEvent.SavePin -> onNavigateBack()
                is SetPinViewModel.UiEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.set_pin),
                onBackClick = {
                    if (state.step == SetPinStep.CONFIRM_PIN) {
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val title = if (state.step == SetPinStep.ENTER_PIN) {
                stringResource(R.string.enter_pin)
            } else {
                stringResource(R.string.confirm_pin)
            }

            val subtitle = if (state.step == SetPinStep.ENTER_PIN) {
                stringResource(R.string.enter_4_digit_pin)
            } else {
                stringResource(R.string.re_enter_pin_to_confirm)
            }

            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                subtitle,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            PinDots(filledCount = state.currentPin.length, total = 4)

            Spacer(Modifier.height(48.dp))

            NumberPad(
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
