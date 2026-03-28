package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.TopBarIconButton
import dev.muffar.moneyfikasi.resource.R

@Composable
fun FilterIcon(
    isFilterApplied: Boolean,
    onClick: () -> Unit,
) {
    Box {
        TopBarIconButton(
            painter = painterResource(R.drawable.ic_filter),
            onClick = onClick
        )

        if (isFilterApplied) {
            Icon(
                imageVector = Icons.Rounded.Circle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .size(10.dp)
                    .align(Alignment.TopStart)
                    .offset(
                        x = 10.dp,
                        y = 5.dp
                    )
            )
        }
    }
}