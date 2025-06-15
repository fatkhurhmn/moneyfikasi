package dev.muffar.moneyfikasi.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
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
                icon = painterResource(R.drawable.ic_transaction),
                label = stringResource(R.string.transaction_menu),
                route = Screen.Transactions.route,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
            )

            BottomBarItem(
                navController = navController,
                icon = painterResource(R.drawable.ic_search),
                label = stringResource(R.string.search_menu),
                route = Screen.Search.route,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
            )

            BottomBarItem(
                navController = navController,
                icon = painterResource(R.drawable.ic_statistic),
                label = stringResource(R.string.statistic_menu),
                route = Screen.Statistic.route,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
            )

            BottomBarItem(
                navController = navController,
                icon = painterResource(R.drawable.ic_settings),
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
    icon: Painter,
    label: String,
    route: String,
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isActive = currentRoute == route

    val iconColor =
        if (!isActive) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary

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
                        popUpTo(Screen.Transactions.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
    ) {
        Icon(
            painter = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(30.dp)
        )

        Text(
            text = label,
            color = iconColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}