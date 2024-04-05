package dev.muffar.moneyfikasi.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ViewList
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.muffar.moneyfikasi.resource.R

@Composable
fun MainBottomNav(
    modifier: Modifier = Modifier,
    containerColor: Color = BottomAppBarDefaults.containerColor.copy(alpha = 0.1f),
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 1.dp,
    contentPadding: PaddingValues = BottomAppBarDefaults.ContentPadding,
    contentHeight: Dp = 80.dp,
    windowInsets: WindowInsets = BottomAppBarDefaults.windowInsets,
    navController: NavHostController,
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        shadowElevation = 0.5.dp,
        modifier = modifier
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .height(contentHeight)
                .padding(contentPadding),
            horizontalArrangement = Arrangement.Absolute.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BottomBarItem(
                navController = navController,
                activeIcon = Icons.AutoMirrored.Rounded.ViewList,
                inactiveIcon = Icons.AutoMirrored.Outlined.ViewList,
                label = stringResource(R.string.transaction_menu),
                route = Screen.Transaction.route,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
            )

            BottomBarItem(
                navController = navController,
                activeIcon = Icons.Rounded.PieChart,
                inactiveIcon = Icons.Outlined.PieChart,
                label = stringResource(R.string.statistics_menu),
                route = Screen.Statistics.route,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
            )

            BottomBarItem(
                navController = navController,
                activeIcon = Icons.Rounded.MonetizationOn,
                inactiveIcon = Icons.Outlined.MonetizationOn,
                label = stringResource(R.string.debt_menu),
                route = Screen.Debt.route,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
            )

            BottomBarItem(
                navController = navController,
                activeIcon = Icons.Rounded.Settings,
                inactiveIcon = Icons.Outlined.Settings,
                label = stringResource(R.string.settings_menu),
                route = Screen.Settings.route,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
            )
        }
    }
}

@Composable
fun BottomBarItem(
    navController: NavHostController,
    activeIcon: ImageVector,
    inactiveIcon : ImageVector,
    label: String,
    route: String,
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isActive = currentRoute == route

    val iconColor = if (!isActive) MaterialTheme.colorScheme.outline.copy(0.9f) else MaterialTheme.colorScheme.onBackground
    val containerColor = if (!isActive) Color.Transparent else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    val labelColor = if (!isActive) MaterialTheme.colorScheme.outline.copy(0.9f) else MaterialTheme.colorScheme.onBackground
    val menuIcon = if (!isActive) inactiveIcon else activeIcon

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (!isActive) {
                    navController.navigate(route) {
                        popUpTo(Screen.Transaction.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .background(color = containerColor)
                .padding(4.dp)
        ) {
            Icon(
                imageVector = menuIcon,
                contentDescription = label,
                tint = iconColor
            )
        }
        Text(
            text = label,
            color = labelColor,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}