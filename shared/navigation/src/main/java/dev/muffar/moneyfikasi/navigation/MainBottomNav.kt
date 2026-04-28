package dev.muffar.moneyfikasi.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.AddTransactionSheet
import dev.muffar.moneyfikasi.common_ui.component.button.AddTransactionButton
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R

@Composable
fun MainBottomNav(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onAddTransaction: (TransactionType?) -> Unit
) {
    val containerColor = BottomAppBarDefaults.containerColor.copy(alpha = 0.1f)
    var showBottomSheet by remember { mutableStateOf(false) }

    Surface(
        color = containerColor,
        contentColor = contentColorFor(containerColor),
        tonalElevation = 1.dp,
        shadowElevation = 0.2.dp,
        modifier = modifier
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(BottomAppBarDefaults.windowInsets)
                .height(65.dp)
                .padding(BottomAppBarDefaults.ContentPadding),
            horizontalArrangement = Arrangement.Absolute.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BottomBarItem(
                navController = navController,
                selectedIcon = painterResource(R.drawable.ic_home_fill),
                unselectedIcon = painterResource(R.drawable.ic_home_outline),
                label = stringResource(R.string.home_menu),
                route = Screen.Home.route,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
            )

            BottomBarItem(
                navController = navController,
                selectedIcon = painterResource(R.drawable.ic_transaction_fill),
                unselectedIcon = painterResource(R.drawable.ic_transaction_outline),
                label = stringResource(R.string.transaction_menu),
                route = Screen.Transactions.route,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
            )

            AddTransactionButton(
                onClick = { showBottomSheet = true },
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
            )

            BottomBarItem(
                navController = navController,
                selectedIcon = painterResource(R.drawable.ic_statistic_fill),
                unselectedIcon = painterResource(R.drawable.ic_statistic_outline),
                label = stringResource(R.string.statistic_menu),
                route = Screen.Statistic.route,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
            )

            BottomBarItem(
                navController = navController,
                selectedIcon = painterResource(R.drawable.ic_more_fill),
                unselectedIcon = painterResource(R.drawable.ic_more_outline),
                label = stringResource(R.string.more_menu),
                route = Screen.More.route,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
            )
        }
    }

    AnimatedVisibility(showBottomSheet) {
        AddTransactionSheet(
            onAddIncome = { onAddTransaction(TransactionType.INCOME) },
            onAddExpense = { onAddTransaction(TransactionType.EXPENSE) },
            onAddTransfer = { onAddTransaction(null) },
            onDismissRequest = { showBottomSheet = false }
        )
    }
}

@Composable
fun BottomBarItem(
    navController: NavHostController,
    selectedIcon: Painter,
    unselectedIcon: Painter,
    label: String,
    route: String,
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isSelected = currentRoute == route

    val iconColor =
        if (!isSelected) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (!isSelected) {
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
    ) {
        Icon(
            painter = if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = iconColor
        )

        Text(
            text = label,
            color = iconColor,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 10.sp
            ),
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 2.dp),
            maxLines = 1
        )
    }
}