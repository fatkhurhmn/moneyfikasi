package dev.muffar.moneyfikasi.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            Text(text = "Categories")
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .padding(vertical = 8.dp)
        ){

        }
    }
}