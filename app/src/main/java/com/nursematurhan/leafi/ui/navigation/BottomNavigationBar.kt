package com.nursematurhan.leafi.ui.navigation

import android.util.Log
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController, isLoggedIn: Boolean) {
    val items = if (isLoggedIn) BottomBarItem.loggedInItems else BottomBarItem.guestItems
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    Log.d("LeafiNav", "Current route: $currentRoute")

    NavigationBar(
        containerColor = Color(0xFFEFEEEA)
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo("plants") {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(item.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF0E8D50),
                    unselectedIconColor = Color(0xFF0E8D50),
                    selectedTextColor = Color(0xFF0E8D50),
                    unselectedTextColor = Color(0xFF0E8D50),
                    indicatorColor = Color(0xFFEFEEEA)
                )
            )
        }
    }
}
