package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.ErrorMessage

@Composable
fun TextInputError(error: ErrorMessage) {
    AnimatedVisibility(
        visible = error.message != null,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
        val shakeAnim = remember { Animatable(0f) }
        LaunchedEffect(error) {
            shakeAnim.animateTo(
                targetValue = 10f,
                animationSpec = tween(
                    durationMillis = 50,
                    easing = EaseInOutSine
                )
            )
            shakeAnim.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 50,
                    easing = EaseInOutSine
                )
            )
        }
        Text(
            text = error.message ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .offset(x = shakeAnim.value.dp)
                .padding(top = 4.dp)
        )
    }
}