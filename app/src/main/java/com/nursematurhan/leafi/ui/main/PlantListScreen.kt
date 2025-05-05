package com.nursematurhan.leafi.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.nursematurhan.leafi.data.model.Plant

@Composable
fun PlantListScreen(
    navController: androidx.navigation.NavHostController,
    onPlantClick: (Plant) -> Unit,
    viewModel: PlantListViewModel = viewModel()
) {
    var filter by remember { mutableStateOf("Tüm Bitkiler") }
    var searchQuery by remember { mutableStateOf("") }
    val filterOptions = listOf("Tüm Bitkiler", "Sık Sulama (<5 gün)", "Orta (5–9 gün)", "Seyrek (10+ gün)")
    var expanded by remember { mutableStateOf(false) }

    val plantList by viewModel.plants.collectAsState()

    val filteredPlants = plantList.filter { plant ->
        val matchesFilter = when (filter) {
            "Sık Sulama (<5 gün)" -> plant.wateringInterval < 5
            "Orta (5–9 gün)" -> plant.wateringInterval in 5..9
            "Seyrek (10+ gün)" -> plant.wateringInterval >= 10
            else -> true
        }
        val matchesSearch = plant.name.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesSearch
    }

    Column(modifier = Modifier.padding(12.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            placeholder = { Text("Bitki ismi ara") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(filter)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                filterOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            filter = option
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(filteredPlants) { plant ->
                PlantCard(plant = plant, onClick = { onPlantClick(plant) })
            }
        }
    }
}

@Composable
fun PlantCard(plant: Plant, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EDF1))
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(plant.imageUrl),
                contentDescription = plant.name,
                modifier = Modifier
                    .size(64.dp)
                    .aspectRatio(1f)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = plant.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Water every ${plant.wateringInterval} days",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}
