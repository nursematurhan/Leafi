package com.nursematurhan.leafi.ui.plants

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.nursematurhan.leafi.data.model.Plant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFromListScreen(
    plant: Plant,
    onAdd: (wateringInterval: Int, firstWateredDate: Long) -> Unit,
    onBack: () -> Unit
) {
    val today = System.currentTimeMillis()
    val defaultInterval = 3

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plant Detail") },
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
            Text("Watering Interval: $defaultInterval days")
            Text("First watering will start today.")

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    onAdd(defaultInterval, today)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to My Plants")
            }
        }
    }
}
