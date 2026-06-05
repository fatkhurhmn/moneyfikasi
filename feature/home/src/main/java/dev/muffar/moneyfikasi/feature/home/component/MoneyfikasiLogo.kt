package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.top_bar.TopBarButton
import dev.muffar.moneyfikasi.resource.R
import java.util.Calendar

@Composable
fun MoneyfikasiLogo(
    modifier: Modifier = Modifier,
    onDashboardSettingsClick: () -> Unit,
) {
    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 5..11 -> R.string.greeting_good_morning
        in 12..17 -> R.string.greeting_good_afternoon
        else -> R.string.greeting_good_evening
    }

    Row(
        modifier = modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            val textLogo = if (isSystemInDarkTheme()) {
                painterResource(R.drawable.text_logo_white)
            } else {
                painterResource(R.drawable.text_logo)
            }
            Image(
                painter = textLogo,
                contentDescription = null,
                modifier = Modifier.width(120.dp),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = stringResource(greeting),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        TopBarButton(Icons.Rounded.MoreVert) { onDashboardSettingsClick() }
    }
}
