package dev.muffar.moneyfikasi.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
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
        Image(
            painter = if (isSystemInDarkTheme()){
                painterResource(R.drawable.splashscreen_logo_white)
            } else {
                painterResource(R.drawable.splashscreen_logo)
            },
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .widthIn(max = 520.dp),
            contentScale = ContentScale.Fit
        )
    }
}
