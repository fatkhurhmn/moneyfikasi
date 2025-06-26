package dev.muffar.moneyfikasi.common_ui.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
object MainColor {
    val White = Color(0xFFFFFFFF)
    val ExtraLightGray = Color(0xFFEBEBF0)
    val LightGray = Color(0xFFCBCBD6)
    val Gray = Color(0xFF74747A)
    val DarkGray = Color(0xFF303033)
    val ExtraDarkGray = Color(0xFF1C1C1F)
    val Black = Color(0xFF09090A)

    val Red = ColorShades(
        extraLight = Color(0xFFFFCDD2),
        light = Color(0xFFEF9A9A),
        kindaLight = Color(0xFFE57373),
        primary = Color(0xFFF44336),
        kindaDark = Color(0xFFD32F2F),
        dark = Color(0xFFC62828),
        extraDark = Color(0xFFB71C1C)
    )

    val Pink = ColorShades(
        extraLight = Color(0xFFF8BBD0),
        light = Color(0xFFF48FB1),
        kindaLight = Color(0xFFF06292),
        primary = Color(0xFFE91E63),
        kindaDark = Color(0xFFC2185B),
        dark = Color(0xFFAD1457),
        extraDark = Color(0xFF880E4F)
    )

    val Purple = ColorShades(
        extraLight = Color(0xFFE1BEE7),
        light = Color(0xFFCE93D8),
        kindaLight = Color(0xFFBA68C8),
        primary = Color(0xFF9C27B0),
        kindaDark = Color(0xFF7B1FA2),
        dark = Color(0xFF6A1B9A),
        extraDark = Color(0xFF4A148C)
    )

    val Blue = ColorShades(
        extraLight = Color(0xFFBBDEFB),
        light = Color(0xFF90CAF9),
        kindaLight = Color(0xFF64B5F6),
        primary = Color(0xFF2196F3),
        kindaDark = Color(0xFF1976D2),
        dark = Color(0xFF1565C0),
        extraDark = Color(0xFF003566)
    )

    val Green = ColorShades(
        extraLight = Color(0xFFC8E6C9),
        light = Color(0xFFA5D6A7),
        kindaLight = Color(0xFF81C784),
        primary = Color(0xFF4CAF50),
        kindaDark = Color(0xFF388E3C),
        dark = Color(0xFF2E7D32),
        extraDark = Color(0xFF1B5E20)
    )

    val Yellow = ColorShades(
        extraLight = Color(0xFFFFF9C4),
        light = Color(0xFFFFF59D),
        kindaLight = Color(0xFFFFF176),
        primary = Color(0xFFFFEB3B),
        kindaDark = Color(0xFFFFC300),
        dark = Color(0xFFF9A825),
        extraDark = Color(0xFFF57F17)
    )

    val Orange = ColorShades(
        extraLight = Color(0xFFFFCCBC),
        light = Color(0xFFFFAB91),
        kindaLight = Color(0xFFFF8A65),
        primary = Color(0xFFFF5722),
        kindaDark = Color(0xFFE64A19),
        dark = Color(0xFFD84315),
        extraDark = Color(0xFFBF360C)
    )
}