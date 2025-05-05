package com.nursematurhan.leafi.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.nursematurhan.leafi.ui.auth.*
import com.nursematurhan.leafi.ui.main.*
import com.nursematurhan.leafi.ui.plants.MyPlantsScreen
import com.nursematurhan.leafi.ui.profile.ProfileScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "profile" else "home",
        modifier = modifier
    ) {
        composable("home") { HomeScreen() }
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("register") { RegisterScreen(navController, authViewModel) }

        composable("plants") {
            PlantListScreen(
                navController = navController,
                onPlantClick = { plant ->
                    val plantJson = Uri.encode(Gson().toJson(plant))
                    navController.navigate("plantDetail/$plantJson")
                }
            )
        }

        composable("profile") {
            ProfileScreen(authViewModel = authViewModel)
        }

        composable("myplants") { MyPlantsScreen() }
    }
}
