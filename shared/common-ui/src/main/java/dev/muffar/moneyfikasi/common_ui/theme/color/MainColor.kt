package dev.muffar.moneyfikasi.common_ui.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
object MainColor {

    // Brand / Primary
    val Primary = Color(0xFFC6F00D)
    val PrimaryHover = Color(0xFFB2DB0C)
    val PrimaryContainerLight = Color(0xFFEAF8B1)
    val PrimaryContainerDark = Color(0xFF2A3408)
    val OnPrimary = Color(0xFF121417)

    // Light Theme Surfaces
    val LightBackground = Color(0xFFF7F8FA)
    val LightSurface1 = Color(0xFFFFFFFF)
    val LightSurface2 = Color(0xFFEFF2F5)
    val LightSurface3 = Color(0xFFE4E9EE)
    val LightOutline = Color(0xFFD7DDE4)
    val LightTextPrimary = Color(0xFF121417)
    val LightTextSecondary = Color(0xFF4A5568)

    // Dark Theme Surfaces
    val DarkBackground = Color(0xFF121417)
    val DarkSurface1 = Color(0xFF1C1F22)
    val DarkSurface2 = Color(0xFF25292D)
    val DarkSurface3 = Color(0xFF2F3439)
    val DarkOutline = Color(0xFF3B424A)
    val DarkTextPrimary = Color(0xFFF7F8FA)
    val DarkTextSecondary = Color(0xFFC9D1DA)

    // Semantic Colors
    val Success = Color(0xFF2DC46D)
    val SuccessContainer = Color(0xFFE8F8EF)
    val Error = Color(0xFFEE5040)
    val ErrorContainer = Color(0xFFFDECEB)
    val Warning = Color(0xFFF39C12)
    val WarningContainer = Color(0xFFFEF4E6)
    val Info = Color(0xFF2563EB)

    // Basic Colors
    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)

    val ExtraLightGray = LightBackground

    val YellowDark = Color(0xFFF9A825)
}
