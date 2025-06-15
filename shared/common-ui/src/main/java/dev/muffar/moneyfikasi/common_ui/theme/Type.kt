package dev.muffar.moneyfikasi.common_ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.resource.R

val LexendFontFamily = FontFamily(
    Font(R.font.lexend_light, FontWeight.Normal),
    Font(R.font.lexend_medium, FontWeight.Medium),
    Font(R.font.lexend_semibold, FontWeight.SemiBold),
    Font(R.font.lexend_bold, FontWeight.Bold),
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = LexendFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 64.sp,
        fontSize = 57.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = LexendFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 52.sp,
        fontSize = 45.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = LexendFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 44.sp,
        fontSize = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = LexendFontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = 40.sp,
        fontSize = 32.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = LexendFontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = 36.sp,
        fontSize = 28.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = LexendFontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = 32.sp,
        fontSize = 24.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = LexendFontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 28.sp,
        fontSize = 22.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = LexendFontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp

    ),
    titleSmall = TextStyle(
        fontFamily = LexendFontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    labelLarge = TextStyle(
        fontFamily = LexendFontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
        fontSize = 16.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = LexendFontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = LexendFontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp,
        fontSize = 11.sp,
        letterSpacing = 0.5.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = LexendFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = LexendFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = LexendFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    ),
)