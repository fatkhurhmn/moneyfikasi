package dev.muffar.moneyfikasi.common_ui.component.message

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.withTimeoutOrNull

@Composable
fun SnackbarMessage(
    state: SnackbarHostState,
) {
    SnackbarHost(hostState = state) { snackbarData ->

        val visuals = snackbarData.visuals as? MessageSnackbarVisuals
        val type = visuals?.type ?: SnackbarType.PLAIN
        val icon = when (type) {
            SnackbarType.SUCCESS -> painterResource(R.drawable.ic_check)
            SnackbarType.ERROR -> painterResource(R.drawable.ic_cross)
            SnackbarType.PLAIN -> null
        }

        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                if (icon != null) {
                    Image(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                }
                Text(
                    text = snackbarData.visuals.message,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

suspend fun SnackbarHostState.showMessage(message: String, type: SnackbarType) {
    currentSnackbarData?.dismiss()
    withTimeoutOrNull(1000) {
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
