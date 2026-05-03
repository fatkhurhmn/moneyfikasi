package dev.muffar.moneyfikasi.common_ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import dev.muffar.moneyfikasi.common_ui.theme.color.FinanceColors
import dev.muffar.moneyfikasi.common_ui.theme.color.LocalFinanceColors
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor


val MoneyfikasiLightColorScheme = lightColorScheme(

    primary             = MainColor.Lime,
    onPrimary           = MainColor.Black,
    primaryContainer    = MainColor.LimeExtraLight,
    onPrimaryContainer  = MainColor.LimeExtraDark,

    secondary           = MainColor.Green,
    onSecondary         = MainColor.White,
    secondaryContainer  = MainColor.GreenExtraLight,
    onSecondaryContainer = MainColor.GreenExtraDark,

    tertiary            = MainColor.Orange,
    onTertiary          = MainColor.White,
    tertiaryContainer   = MainColor.OrangeExtraLight,
    onTertiaryContainer = MainColor.OrangeExtraDark,

    error               = MainColor.Red,
    onError             = MainColor.White,
    errorContainer      = MainColor.RedExtraLight,
    onErrorContainer    = MainColor.RedExtraDark,

    background          = MainColor.ExtraLightGray,
    onBackground        = MainColor.ExtraDarkGray,

    surface             = MainColor.White,
    onSurface           = MainColor.ExtraDarkGray,
    surfaceVariant      = MainColor.ExtraLightGray,
    onSurfaceVariant    = MainColor.DarkGray,

    outline             = MainColor.LightGray,
    outlineVariant      = MainColor.ExtraLightGray,

    inverseSurface      = MainColor.Charcoal,
    inverseOnSurface    = MainColor.ExtraLightGray,
    inversePrimary      = MainColor.Lime,

    scrim               = MainColor.Black,
)


val MoneyfikasiDarkColorScheme = darkColorScheme(
    primary             = MainColor.Lime,
    onPrimary           = MainColor.Black,
    primaryContainer    = MainColor.LimeExtraDark,
    onPrimaryContainer  = MainColor.LimeExtraLight,

    secondary           = MainColor.GreenKindaLight,
    onSecondary         = MainColor.GreenExtraDark,
    secondaryContainer  = MainColor.GreenDark,
    onSecondaryContainer = MainColor.GreenExtraLight,

    tertiary            = MainColor.OrangeKindaLight,
    onTertiary          = MainColor.OrangeExtraDark,
    tertiaryContainer   = MainColor.OrangeDark,
    onTertiaryContainer = MainColor.OrangeExtraLight,

    error               = MainColor.RedKindaLight,
    onError             = MainColor.RedExtraDark,
    errorContainer      = MainColor.RedDark,
    onErrorContainer    = MainColor.RedExtraLight,

    background          = MainColor.SurfaceDarkPage,
    onBackground        = MainColor.ExtraLightGray,

    surface             = MainColor.SurfaceDarkCard,
    onSurface           = MainColor.White,
    surfaceVariant      = MainColor.SurfaceDarkRow,
    onSurfaceVariant    = MainColor.Gray,

    outline             = MainColor.DarkGray,
    outlineVariant      = MainColor.SurfaceDarkRow,

    inverseSurface      = MainColor.ExtraLightGray,
    inverseOnSurface    = MainColor.Charcoal,
    inversePrimary      = MainColor.CharcoalLight,

    scrim               = MainColor.Black,
)

@Composable
fun MoneyfikasiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) MoneyfikasiDarkColorScheme else MoneyfikasiLightColorScheme

    val financeColors = if (darkTheme) {
        FinanceColors(
            income = MainColor.GreenKindaLight,
            incomeContainer = MainColor.GreenDark,
            onIncomeContainer = MainColor.GreenExtraLight,
            expense = MainColor.RedKindaLight,
            expenseContainer = MainColor.RedDark,
            onExpenseContainer = MainColor.RedExtraLight,
            budget = MainColor.OrangeKindaLight,
            budgetContainer = MainColor.OrangeDark,
            onBudgetContainer = MainColor.OrangeExtraLight,
            brand = MainColor.Lime,
            brandContainer = MainColor.LimeExtraDark,
            onBrandContainer = MainColor.LimeExtraLight,
            brandKindaLight = MainColor.LimeKindaLight,
            brandDark = MainColor.LimeDark,
            info = MainColor.BlueKindaLight,
            warning = MainColor.YellowKindaDark,
            chartColors = listOf(
                MainColor.Lime,
                MainColor.GreenKindaLight,
                MainColor.BlueKindaLight,
                MainColor.OrangeKindaLight,
                MainColor.RedKindaLight,
            )
        )
    } else {
        FinanceColors(
            income = MainColor.Green,
            incomeContainer = MainColor.GreenExtraLight,
            onIncomeContainer = MainColor.GreenExtraDark,
            expense = MainColor.Red,
            expenseContainer = MainColor.RedExtraLight,
            onExpenseContainer = MainColor.RedExtraDark,
            budget = MainColor.Orange,
            budgetContainer = MainColor.OrangeExtraLight,
            onBudgetContainer = MainColor.OrangeExtraDark,
            brand = MainColor.Lime,
            brandContainer = MainColor.LimeExtraLight,
            onBrandContainer = MainColor.LimeExtraDark,
            brandKindaLight = MainColor.LimeKindaLight,
            brandDark = MainColor.LimeDark,
            info = MainColor.BlueKindaLight,
            warning = MainColor.YellowKindaDark,
            chartColors = listOf(
                MainColor.Lime,
                MainColor.Green,
                MainColor.Blue,
                MainColor.Orange,
                MainColor.Red,
            )
        )
    }

    CompositionLocalProvider(
        LocalFinanceColors provides financeColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}

object MoneyfikasiTheme {
    val financeColors: FinanceColors
        @Composable
        get() = LocalFinanceColors.current
}
