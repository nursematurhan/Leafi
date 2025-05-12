package com.nursematurhan.leafi.ui.myplants

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPlantsScreen(
    navController: NavHostController,
    viewModel: MyPlantsViewModel = viewModel()
) {
    val myPlants by viewModel.myPlants.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Plants") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFEFEEEA),
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding).padding(12.dp)) {
            items(myPlants) { plant ->
                MyPlantCard(
                    plant = plant,
                    onClick = {
                        val plantJson = Uri.encode(Gson().toJson(plant))
                        navController.navigate("myplant_detail/$plantJson")
                    },
                    onWatered = {
                        viewModel.markAsWatered(plant.plantId)
                    }
                )
            }
        }
    }
}

@Composable
fun MyPlantCard(
    plant: MyPlant,
    onClick: () -> Unit,
    onWatered: () -> Unit
) {
    val daysSinceWatered = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - plant.lastWateredDate)
    val remainingDays = plant.wateringIntervalDays - daysSinceWatered

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (remainingDays < 0) Color(0xFFFFCDD2) else Color(0xFFE8F5E9)
        )
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(plant.plantImageUrl),
                contentDescription = plant.plantName,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = plant.plantName, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = when {
                        remainingDays > 0 -> "Water in $remainingDays days"
                        remainingDays == 0L -> "Water today!"
                        else -> "Overdue by ${-remainingDays} days"
                    },
                    color = if (remainingDays < 0) Color.Red else Color.Black
                )
            }
            Button(onClick = onWatered) {
                Text("Watered")
            }
        }
    }
}
