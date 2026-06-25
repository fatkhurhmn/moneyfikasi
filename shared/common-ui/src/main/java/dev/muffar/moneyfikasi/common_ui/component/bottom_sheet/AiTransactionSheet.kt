package dev.muffar.moneyfikasi.common_ui.component.bottom_sheet

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonButton
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiTransactionSheet(
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
    isProcessing: Boolean = false,
    error: String? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    val infiniteTransition = rememberInfiniteTransition(label = "rainbow")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset"
    )

    val rainbowBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF4285F4),
            Color(0xFF9B72CB),
            Color(0xFFD96570),
            Color(0xFFF4AF5F),
            Color(0xFF4285F4)
        ),
        start = Offset(offset, 0f),
        end = Offset(offset + 1000f, 1000f),
        tileMode = TileMode.Repeated
    )

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
    }

    val hideSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest()
            }
        }
    }

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = { hideSheet() },
        sheetState = sheetState,
        sheetGesturesEnabled = !isProcessing
    ) {
        BottomSheetTitle(stringResource(R.string.title_ai_transaction))
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.msg_ai_transaction_description),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                CommonTextInput(
                    value = text,
                    onValueChange = { text = it },
                    label = stringResource(R.string.label_ai_prompt),
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (isProcessing) Modifier.blur(6.dp) else Modifier
                        ),
                    enabled = !isProcessing,
                    maxLines = 5,
                    focusRequester = focusRequester
                )

                if (isProcessing) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(rainbowBrush, alpha = 0.15f)
                            .border(
                                width = 2.dp,
                                brush = rainbowBrush,
                                shape = MaterialTheme.shapes.medium
                            )
                    )
                    Text(
                        text = stringResource(R.string.msg_ai_processing),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
            if (!error.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            CommonButton(
                text = stringResource(R.string.action_process),
                onClick = { onConfirm(text) },
                enabled = text.isNotBlank() && !isProcessing,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
