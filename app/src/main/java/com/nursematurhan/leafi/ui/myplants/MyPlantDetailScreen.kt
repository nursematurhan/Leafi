package com.nursematurhan.leafi.ui.myplants

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPlantDetailScreen(
    myPlant: MyPlant,
    onBack: () -> Unit,
    onWatered: () -> Unit,
    onDelete: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }

    val daysSinceWatered =
        TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - myPlant.lastWateredDate)
    val remainingDays = myPlant.wateringIntervalDays - daysSinceWatered
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    val lastWateredDate = formatter.format(Date(myPlant.lastWateredDate))
    val nextWateredDate =
        formatter.format(Date(myPlant.lastWateredDate + myPlant.wateringIntervalDays * 86400000L))

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar("Plant successfully removed from your list.")
            showSnackbar = false
            onBack()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Plant Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Plant",
                            tint = Color.Red
                        )
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
                painter = rememberAsyncImagePainter(myPlant.plantImageUrl),
                contentDescription = myPlant.plantName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = myPlant.plantName, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Last watered: $lastWateredDate")
            Text(text = "Next watering: $nextWateredDate")
            Text(text = "Watering interval: ${myPlant.wateringIntervalDays} days")

            Spacer(modifier = Modifier.height(24.dp))

            if (remainingDays <= 0) {
                Button(
                    onClick = onWatered,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Watered")
                }
            } else {
                Text(
                    text = "You can water this plant in $remainingDays day(s).",
                    color = Color.Gray
                )
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Plant") },
                text = { Text("Are you sure you want to delete this plant?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            onDelete()
                            showSnackbar = true
                        }
                    ) {
                        Text("Yes", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
