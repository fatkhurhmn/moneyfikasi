package dev.muffar.moneyfikasi.common_ui.component.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CalculatorResultDisplay(
    input: String,
    history: String,
    modifier: Modifier = Modifier
) {
    val txtScale = remember { Animatable(1f) }

    LaunchedEffect(history) {
        if (history.isNotEmpty()) {
            txtScale.animateTo(
                targetValue = 1.1f,
                animationSpec = tween(durationMillis = 100)
            )
            txtScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End
    ) {

        AnimatedContent(
            targetState = history,
            transitionSpec = { fadeIn().togetherWith(fadeOut()) },
            label = "HistoryAnimation"
        ) { targetHistory ->
            Text(
                text = formatWithCommas(targetHistory),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End,
                lineHeight = 24.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = formatWithCommas(input),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            maxLines = 5,
        )
    }
}

fun formatWithCommas(input: String): String {
    if (input == "Error") return input

    val regex = Regex("\\d+\\.?\\d*")

    return regex.replace(input) { matchResult ->
        val numberStr = matchResult.value
        if (numberStr.contains(".")) {
            val parts = numberStr.split(".")
            val integerPart = parts[0].toLongOrNull() ?: 0
            val decimalPart = parts.getOrNull(1) ?: ""

            val formattedInt = DecimalFormat("#,###").format(integerPart)
            if (parts.size > 1 || numberStr.endsWith(".")) "$formattedInt.$decimalPart" else formattedInt
        } else {
            val number = numberStr.toLongOrNull()
            if (number != null) DecimalFormat("#,###").format(number) else numberStr
        }
    }
}