package dev.muffar.moneyfikasi.common_ui.component.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import kotlinx.coroutines.withTimeoutOrNull

@Composable
fun SnackbarMessage(
    state: SnackbarHostState,
) {
    SnackbarHost(hostState = state) { snackbarData ->

        val visuals = snackbarData.visuals as? MessageSnackbarVisuals
        val type = visuals?.type ?: SnackbarType.PLAIN
        val icon = when (type) {
            SnackbarType.SUCCESS -> Icons.Rounded.Check
            SnackbarType.ERROR -> Icons.Rounded.Close
            SnackbarType.PLAIN -> null
        }

        val financeColors = MoneyfikasiTheme.financeColors
        val tint = when (type) {
            SnackbarType.SUCCESS -> financeColors.income
            SnackbarType.ERROR -> financeColors.expense
            SnackbarType.PLAIN -> financeColors.info
        }

        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                if (icon != null) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(tint),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                }
                Text(
                    text = snackbarData.visuals.message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

suspend fun SnackbarHostState.showMessage(message: String, type: SnackbarType) {
    currentSnackbarData?.dismiss()
    withTimeoutOrNull(2000) {
        showSnackbar(
            MessageSnackbarVisuals(
                message = message,
                type = type,
                duration = SnackbarDuration.Indefinite,
                withDismissAction = false
            )
        )
    }
}
