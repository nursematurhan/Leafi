package com.nursematurhan.leafi.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.nursematurhan.leafi.data.PlantRepository
import com.nursematurhan.leafi.data.model.Plant
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlantScreen(
    navController: NavHostController,
    onAddPlant: (Plant) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var wateringInterval by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Plant Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("Image URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = wateringInterval,
            onValueChange = { wateringInterval = it },
            label = { Text("Watering Interval (days)") },
            modifier = Modifier.fillMaxWidth()
        )

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val interval = wateringInterval.toIntOrNull()
                if (name.isNotBlank() && imageUrl.isNotBlank() && interval != null) {
                    val newPlant = Plant(name = name, imageUrl = imageUrl, wateringInterval = interval)

                    // Firestore’a ekle
                    PlantRepository.addPlant(
                        newPlant,
                        onSuccess = {
                            onAddPlant(newPlant)
                            navController.popBackStack()
                        },
                        onFailure = {
                            errorMessage = "Error: ${it.localizedMessage}"
                        }
                    )
                } else {
                    errorMessage = "Please fill in all fields correctly."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Plant")
        }
    }
}

