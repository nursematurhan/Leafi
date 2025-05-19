package com.nursematurhan.leafi.ui.navigation

import android.net.Uri
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.nursematurhan.leafi.ui.auth.*
import com.nursematurhan.leafi.ui.home.HomeScreen
import com.nursematurhan.leafi.ui.myplants.MyPlant
import com.nursematurhan.leafi.ui.myplants.MyPlantDetailScreen
import com.nursematurhan.leafi.ui.myplants.MyPlantsScreen
import com.nursematurhan.leafi.ui.myplants.MyPlantsViewModel
import com.nursematurhan.leafi.ui.myplants.*
import com.nursematurhan.leafi.ui.plants.AddFromListScreen
import com.nursematurhan.leafi.ui.plants.PlantListScreen
import com.nursematurhan.leafi.ui.plants.PlantListViewModel
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
        composable("home") { HomeScreen(navController = navController) }

        composable("login") { LoginScreen(navController, authViewModel) }
        composable("register") { RegisterScreen(navController, authViewModel) }

        composable("plants") {
            val plantListViewModel: PlantListViewModel = viewModel()
            PlantListScreen(
                navController = navController,
                onPlantClick = { plant ->
                    navController.navigate("add_from_list/${plant.id}")
                }
            )
        }

        composable("add_from_list/{plantId}") { backStackEntry ->
            val plantId = backStackEntry.arguments?.getString("plantId") ?: return@composable
            val plantListViewModel: PlantListViewModel = viewModel()
            val plant = plantListViewModel.getPlantById(plantId)

            if (plant != null) {
                AddFromListScreen(
                    plant = plant,
                    onAdd = { interval, date ->
                        plantListViewModel.addToMyPlants(plant, interval, date)
                    },
                    onBack = { navController.popBackStack() },
                    isLoggedIn = isLoggedIn,
                    onGoToRegister = { navController.navigate("register") },
                    onNavigateToMyPlants = {
                        navController.navigate("myplants") {
                            popUpTo("plants") { inclusive = false }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            } else {
                Text("Plant not found.")
            }
        }


        composable("myplants") {
            MyPlantsScreen(navController = navController)
        }

        composable("myplant_detail/{plantJson}") { backStackEntry ->
            val plantJson = backStackEntry.arguments?.getString("plantJson")
            val decodedJson = Uri.decode(plantJson ?: return@composable)
            val myPlant = Gson().fromJson(decodedJson, MyPlant::class.java)

            val myPlantsViewModel: MyPlantsViewModel = viewModel() // BU ÖNEMLİ!

            MyPlantDetailScreen(
                myPlant = myPlant,
                onBack = { navController.popBackStack() },
                onWatered = {
                    myPlantsViewModel.markAsWatered(myPlant.plantId)
                    navController.popBackStack()
                },
                onDelete = {
                    myPlantsViewModel.deletePlant(
                        myPlant.plantId,
                        onSuccess = { navController.popBackStack() },
                        onFailure = {

                        }
                    )
                }
            )
        }


        composable("profile") {
            ProfileScreen(authViewModel = authViewModel)
        }
    }
}
