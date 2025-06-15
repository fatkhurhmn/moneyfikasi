package dev.muffar.moneyfikasi.common_ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor

private val DarkColorScheme = darkColorScheme(
    primary = MainColor.Green.primary,
    onPrimary = MainColor.White,
    primaryContainer = MainColor.Green.light,
    onPrimaryContainer = MainColor.White,
    inversePrimary = MainColor.Green.dark,

    secondary = MainColor.Yellow.kindaDark,
    onSecondary = MainColor.White,
    secondaryContainer = MainColor.Yellow.light,
    onSecondaryContainer = MainColor.White,

    tertiary = MainColor.Blue.primary,
    onTertiary = MainColor.White,
    tertiaryContainer = MainColor.Blue.light,
    onTertiaryContainer = MainColor.White,

    error = MainColor.Red.primary,
    onError = MainColor.White,
    errorContainer = MainColor.Red.light,
    onErrorContainer = MainColor.White,

    background = MainColor.Black,
    onBackground = MainColor.White,
    surface = MainColor.Black,
    onSurface = MainColor.White,
    surfaceVariant = MainColor.ExtraDarkGray,
    onSurfaceVariant = MainColor.White,
    surfaceTint = MainColor.White,
    inverseSurface = MainColor.LightGray,
    inverseOnSurface = MainColor.Black,

    outline = MainColor.Gray,
    outlineVariant = MainColor.LightGray,
    scrim = MainColor.ExtraLightGray.copy(alpha = 0.8f)
)

private val LightColorScheme = lightColorScheme(
    primary = MainColor.Blue.dark,
    onPrimary = MainColor.White,
    primaryContainer = MainColor.Blue.dark,
    onPrimaryContainer = MainColor.White,
    inversePrimary = MainColor.Blue.light,

    secondary = MainColor.Yellow.kindaDark,
    onSecondary = MainColor.White,
    secondaryContainer = MainColor.Yellow.light,
    onSecondaryContainer = MainColor.White,

    tertiary = MainColor.Orange.primary,
    onTertiary = MainColor.White,
    tertiaryContainer = MainColor.Orange.light,
    onTertiaryContainer = MainColor.White,

    error = MainColor.Red.primary,
    onError = MainColor.White,
    errorContainer = MainColor.Red.light,
    onErrorContainer = MainColor.White,

    background = MainColor.White,
    onBackground = MainColor.Black,
    surface = MainColor.White,
    onSurface = MainColor.Black,
    surfaceVariant = MainColor.ExtraLightGray,
    onSurfaceVariant = MainColor.Black,
    surfaceTint = MainColor.Black,
    inverseSurface = MainColor.DarkGray,
    inverseOnSurface = MainColor.White,

    outline = MainColor.Gray,
    outlineVariant = MainColor.DarkGray,
    scrim = MainColor.ExtraDarkGray.copy(alpha = 0.8f)
)

@Composable
fun MoneyfikasiTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content,
        shapes = Shapes
    )
}