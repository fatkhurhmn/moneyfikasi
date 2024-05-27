package dev.muffar.moneyfikasi.search.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R

@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.padding(vertical = 4.dp)) {
        Text(
            text = stringResource(R.string.search_menu),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}