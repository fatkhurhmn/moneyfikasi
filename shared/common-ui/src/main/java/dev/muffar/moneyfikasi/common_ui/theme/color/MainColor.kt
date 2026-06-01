package dev.muffar.moneyfikasi.common_ui.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
object MainColor {

    // Brand / Primary
    val Primary = Color(0xFFA3E635)
    val PrimaryHover = Color(0xFF84CC16)
    val PrimaryContainerLight = Color(0xFFECFCCB)
    val PrimaryContainerDark = Color(0xFF374151)
    val OnPrimary = Color(0xFF1A2E05)
    val OnPrimaryContainer = Color(0xFF1A2E05)
    val OnPrimaryContainerDark = Color(0xFFF8FAFC)
    val DarkPrimary = Primary
    val OnDarkPrimary = OnPrimary

    // Secondary
    val SecondaryLight = Color(0xFF334155)
    val OnSecondaryLight = Color(0xFFFFFFFF)
    val SecondaryContainerLight = Color(0xFFE2E8F0)
    val OnSecondaryContainerLight = Color(0xFF111827)
    val SecondaryDark = Color(0xFFE2E8F0)
    val OnSecondaryDark = Color(0xFF111827)
    val SecondaryContainerDark = Color(0xFF334155)
    val OnSecondaryContainerDark = Color(0xFFF8FAFC)

    // Light Theme Surfaces
    val LightBackground = Color(0xFFF7F8F6)
    val LightSurface1 = Color(0xFFFFFFFF)
    val LightSurface2 = Color(0xFFEFF2EF)
    val LightSurface3 = Color(0xFFE1E5E0)
    val LightOutline = Color(0xFFD0D7D0)
    val LightTextPrimary = Color(0xFF111827)
    val LightTextSecondary = Color(0xFF64748B)

    // Dark Theme Surfaces
    val DarkBackground = Color(0xFF0B0D10)
    val DarkSurface1 = Color(0xFF15191F)
    val DarkSurface2 = Color(0xFF20262E)
    val DarkSurface3 = Color(0xFF313A45)
    val DarkOutline = Color(0xFF4C5663)
    val DarkTextPrimary = Color(0xFFF9FAFB)
    val DarkTextSecondary = Color(0xFFC2C8D0)

    // Light Surface Containers
    val LightSurfaceContainerLowest = Color(0xFFFFFFFF)
    val LightSurfaceContainerLow = Color(0xFFF7F8F6)
    val LightSurfaceContainer = Color(0xFFEFF2EF)
    val LightSurfaceContainerHigh = Color(0xFFE1E5E0)

    // Dark Surface Containers
    val DarkSurfaceContainerLowest = Color(0xFF101317)
    val DarkSurfaceContainerLow = Color(0xFF15191F)
    val DarkSurfaceContainer = Color(0xFF20262E)
    val DarkSurfaceContainerHigh = Color(0xFF313A45)

    // Semantic Colors
    val Success = Color(0xFF7BC96F)
    val SuccessContainer = Color(0xFFF0FAEC)
    val Error = Color(0xFFFF8A7A)
    val ErrorContainer = Color(0xFFFFEEEB)
    val Warning = Color(0xFFFFB020)
    val WarningContainer = Color(0xFFFFF1C2)
    val Info = Color(0xFF0891B2)

    // Basic Colors
    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)

    val YellowDark = Warning
}
