package dev.muffar.moneyfikasi.common_ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import dev.muffar.moneyfikasi.common_ui.theme.color.FinanceColors
import dev.muffar.moneyfikasi.common_ui.theme.color.LocalFinanceColors
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor

val MoneyfikasiLightColorScheme = lightColorScheme(
    primary = MainColor.Primary,
    onPrimary = MainColor.OnPrimary,
    primaryContainer = MainColor.PrimaryContainerLight,
    onPrimaryContainer = MainColor.OnPrimary,

    secondary = MainColor.Success,
    onSecondary = MainColor.White,
    secondaryContainer = MainColor.SuccessContainer,
    onSecondaryContainer = MainColor.Success,

    tertiary = MainColor.Warning,
    onTertiary = MainColor.White,
    tertiaryContainer = MainColor.WarningContainer,
    onTertiaryContainer = MainColor.Warning,

    error = MainColor.Error,
    onError = MainColor.White,
    errorContainer = MainColor.ErrorContainer,
    onErrorContainer = MainColor.Error,

    background = MainColor.LightBackground,
    onBackground = MainColor.LightTextPrimary,

    surface = MainColor.LightSurface1,
    onSurface = MainColor.LightTextPrimary,
    surfaceVariant = MainColor.LightSurface2,
    onSurfaceVariant = MainColor.LightTextSecondary,

    surfaceContainerLowest = MainColor.LightSurfaceContainerLowest,
    surfaceContainerLow = MainColor.LightSurfaceContainerLow,
    surfaceContainer = MainColor.LightSurfaceContainer,
    surfaceContainerHigh = MainColor.LightSurfaceContainerHigh,

    outline = MainColor.LightOutline,
    outlineVariant = MainColor.LightSurface3,

    inverseSurface = MainColor.DarkBackground,
    inverseOnSurface = MainColor.LightBackground,
    inversePrimary = MainColor.Primary,

    scrim = MainColor.Black,
)

val MoneyfikasiDarkColorScheme = darkColorScheme(
    primary = MainColor.Primary,
    onPrimary = MainColor.OnPrimary,
    primaryContainer = MainColor.PrimaryContainerDark,
    onPrimaryContainer = MainColor.Primary,

    secondary = MainColor.Success,
    onSecondary = MainColor.White,
    secondaryContainer = MainColor.SuccessContainer,
    onSecondaryContainer = MainColor.Success,

    tertiary = MainColor.Warning,
    onTertiary = MainColor.White,
    tertiaryContainer = MainColor.WarningContainer,
    onTertiaryContainer = MainColor.Warning,

    error = MainColor.Error,
    onError = MainColor.White,
    errorContainer = MainColor.ErrorContainer,
    onErrorContainer = MainColor.Error,

    background = MainColor.DarkBackground,
    onBackground = MainColor.DarkTextPrimary,

    surface = MainColor.DarkSurface1,
    onSurface = MainColor.DarkTextPrimary,
    surfaceVariant = MainColor.DarkSurface2,
    onSurfaceVariant = MainColor.DarkTextSecondary,

    surfaceContainerLowest = MainColor.DarkSurfaceContainerLowest,
    surfaceContainerLow = MainColor.DarkSurfaceContainerLow,
    surfaceContainer = MainColor.DarkSurfaceContainer,
    surfaceContainerHigh = MainColor.DarkSurfaceContainerHigh,

    outline = MainColor.DarkOutline,
    outlineVariant = MainColor.DarkSurface3,

    inverseSurface = MainColor.LightBackground,
    inverseOnSurface = MainColor.DarkBackground,
    inversePrimary = MainColor.Primary,

    scrim = MainColor.Black,
)

val lightFinanceColors = FinanceColors(
    income = MainColor.Success,
    incomeContainer = MainColor.SuccessContainer,
    onIncomeContainer = MainColor.Success,
    expense = MainColor.Error,
    expenseContainer = MainColor.ErrorContainer,
    onExpenseContainer = MainColor.Error,
    budget = MainColor.Warning,
    budgetContainer = MainColor.WarningContainer,
    onBudgetContainer = MainColor.Warning,
    brand = MainColor.Primary,
    brandContainer = MainColor.PrimaryContainerLight,
    onBrandContainer = MainColor.OnPrimary,
    brandKindaLight = MainColor.PrimaryHover,
    brandDark = MainColor.Primary,
    info = MainColor.Info,
    warning = MainColor.Warning,
    chartColors = listOf(
        MainColor.Primary,
        MainColor.Success,
        MainColor.Info,
        MainColor.Warning,
        MainColor.Error,
    )
)

val darkFinanceColors = FinanceColors(
    income = MainColor.Success,
    incomeContainer = MainColor.DarkSurface1,
    onIncomeContainer = MainColor.Success,
    expense = MainColor.Error,
    expenseContainer = MainColor.DarkSurface1,
    onExpenseContainer = MainColor.Error,
    budget = MainColor.Warning,
    budgetContainer = MainColor.DarkSurface1,
    onBudgetContainer = MainColor.Warning,
    brand = MainColor.Primary,
    brandContainer = MainColor.PrimaryContainerDark,
    onBrandContainer = MainColor.Primary,
    brandKindaLight = MainColor.PrimaryHover,
    brandDark = MainColor.Primary,
    info = MainColor.Info,
    warning = MainColor.Warning,
    chartColors = listOf(
        MainColor.Primary,
        MainColor.Success,
        MainColor.Info,
        MainColor.Warning,
        MainColor.Error,
    )
)

object MoneyfikasiTheme {
    val financeColors: FinanceColors
        @Composable
        get() = LocalFinanceColors.current
}

@Composable
fun MoneyfikasiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) MoneyfikasiDarkColorScheme else MoneyfikasiLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as android.app.Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                !darkTheme
        }
    }

    val financeColors = if (darkTheme) darkFinanceColors else lightFinanceColors

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
