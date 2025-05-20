package com.nursematurhan.leafi.plants

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFromListScreen(
    plant: Plant,
    onAdd: (wateringInterval: Int, firstWateredDate: Long) -> Unit,
    onBack: () -> Unit,
    isLoggedIn: Boolean,
    onGoToRegister: () -> Unit,
    onNavigateToMyPlants: () -> Unit
) {
    val today = System.currentTimeMillis()
    val interval = plant.wateringInterval
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf(false) }

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar("Plant successfully added to your list.")
            showSnackbar = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Plant Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(plant.imageUrl),
                contentDescription = plant.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = plant.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = plant.description)

            Spacer(modifier = Modifier.height(24.dp))
            Text("Watering interval: $interval days")
            Text("The first watering will be today.")

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoggedIn) {
                Button(
                    onClick = {
                        onAdd(interval, today)
                        showSnackbar = true
                        onNavigateToMyPlants()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add to My Plants")
                }
            } else {
                Button(
                    onClick = onGoToRegister,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
                ) {
                    Text("Start building your plant list", color = Color.White)
                }
            }
        }
    }
}

