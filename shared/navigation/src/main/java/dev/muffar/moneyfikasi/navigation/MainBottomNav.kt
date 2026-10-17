package dev.muffar.moneyfikasi.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.AddTransactionSheet
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R

@Composable
fun MainBottomNav(
    navController: NavHostController,
    onAddTransaction: (TransactionType?) -> Unit,
    onAddAiTransaction: () -> Unit,
    onAddDebt: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(BottomAppBarDefaults.windowInsets)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .shadow(1.dp, CircleShape)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BottomBarItem(
                navController = navController,
                selectedIcon = painterResource(R.drawable.ic_home_fill),
                unselectedIcon = painterResource(R.drawable.ic_home_outline),
                label = stringResource(R.string.menu_home),
                route = Screen.Home.route,
                modifier = Modifier.weight(1f)
            )

            BottomBarItem(
                navController = navController,
                selectedIcon = painterResource(R.drawable.ic_transaction_fill),
                unselectedIcon = painterResource(R.drawable.ic_transaction_outline),
                label = stringResource(R.string.menu_transactions),
                route = Screen.Transactions.route,
                modifier = Modifier.weight(1f)
            )

            BottomBarItem(
                navController = navController,
                selectedIcon = painterResource(R.drawable.ic_statistic_fill),
                unselectedIcon = painterResource(R.drawable.ic_statistic_outline),
                label = stringResource(R.string.menu_statistic),
                route = Screen.Statistic.route,
                modifier = Modifier.weight(1f)
            )

            BottomBarItem(
                navController = navController,
                selectedIcon = painterResource(R.drawable.ic_more_fill),
                unselectedIcon = painterResource(R.drawable.ic_more_outline),
                label = stringResource(R.string.menu_more),
                route = Screen.More.route,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier.height(56.dp),
            contentAlignment = Alignment.Center
        ) {

            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(1.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.title_create_transaction),
                    modifier = Modifier.size(24.dp)
                )
            }
            SmallFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-58).dp),
                onClick = { onAddAiTransaction() },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(1.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = stringResource(R.string.label_ai_transaction)
                )
            }
        }
    }

    AnimatedVisibility(showBottomSheet) {
        AddTransactionSheet(
            onAddIncomeExpense = { onAddTransaction(TransactionType.EXPENSE) },
            onAddTransfer = { onAddTransaction(null) },
            onAddDebt = { onAddDebt() },
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

    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.surface.copy(0.5f)
    } else {
        Color.Transparent
    }
    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(CircleShape)
            .background(containerColor)
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
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painter = if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = contentColor
        )
    }
}
