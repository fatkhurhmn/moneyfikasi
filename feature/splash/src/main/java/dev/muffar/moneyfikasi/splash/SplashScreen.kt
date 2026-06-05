package dev.muffar.moneyfikasi.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val iconLogo = if (isSystemInDarkTheme()) {
                painterResource(R.drawable.icon_logo_white)
            } else {
                painterResource(R.drawable.icon_logo)
            }

            val textLogo = if (isSystemInDarkTheme()) {
                painterResource(R.drawable.text_logo_white)
            } else {
                painterResource(R.drawable.text_logo)
            }

            Image(
                painter = iconLogo,
                contentDescription = null,
                modifier = Modifier.width(120.dp),
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = textLogo,
                contentDescription = null,
                modifier = Modifier.width(200.dp),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}
