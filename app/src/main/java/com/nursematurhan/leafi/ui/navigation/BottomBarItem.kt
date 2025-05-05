package com.nursematurhan.leafi.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomBarItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    companion object {
        val guestItems = listOf(
            BottomBarItem("Home", Icons.Default.Home, "home"),
            BottomBarItem("Plants", Icons.Default.LocalFlorist, "plants"),
            BottomBarItem("Login", Icons.Default.Person, "login")
        )

        val loggedInItems = listOf(
            BottomBarItem("Profile", Icons.Default.Person, "profile"),
            BottomBarItem("Plants", Icons.Default.LocalFlorist, "plants"),
            BottomBarItem("My Plants", Icons.Default.Favorite, "myplants")
        )
    }
}
