package dev.muffar.moneyfikasi.wallet.add_edit.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.IconByName
import dev.muffar.moneyfikasi.resource.R

@Composable
fun WalletCard(
    name: String,
    color: Long,
    icon: String,
    balance: String,
) {
    val contentColor = if (color != 0L) Color.White else Color.DarkGray
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (color != 0L) Color(color) else Color.Gray.copy(0.2f),
            contentColor = Color.White,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (color != 0L) Color(color) else Color.Gray.copy(0.8f)
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (icon.isNotEmpty()) {
                    IconByName(
                        name = icon,
                        tint = contentColor,
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.QuestionMark,
                        contentDescription = stringResource(R.string.icon),
                        tint = contentColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = name.ifEmpty { stringResource(R.string.wallet_name) },
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = balance,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                color = contentColor
            )
        }
    }
}