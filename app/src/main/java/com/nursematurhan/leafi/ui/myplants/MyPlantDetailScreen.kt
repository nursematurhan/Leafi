package com.nursematurhan.leafi.ui.myplants

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    var showDeleteSnackbar by remember { mutableStateOf(false) }
    var showWateredSnackbar by remember { mutableStateOf(false) }
    var showConfirmEarlyWaterDialog by remember { mutableStateOf(false) }

    val daysSinceWatered =
        TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - myPlant.lastWateredDate)
    val remainingDays = myPlant.wateringIntervalDays - daysSinceWatered
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    val lastWateredDate = formatter.format(Date(myPlant.lastWateredDate))
    val nextWateredDate =
        formatter.format(Date(myPlant.lastWateredDate + myPlant.wateringIntervalDays * 86400000L))

    LaunchedEffect(showDeleteSnackbar) {
        if (showDeleteSnackbar) {
            snackbarHostState.showSnackbar("Plant successfully removed from your list.")
            showDeleteSnackbar = false
            onBack()
        }
    }

    LaunchedEffect(showWateredSnackbar) {
        if (showWateredSnackbar) {
            snackbarHostState.showSnackbar("Watering date updated!")
            showWateredSnackbar = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("My Plant Detail") },
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
                    .heightIn(min = 200.dp, max = 300.dp)
                    .aspectRatio(1.5f)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = myPlant.plantName, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Last watered: $lastWateredDate")
            Text(text = "Next watering: $nextWateredDate")
            Text(text = "Watering interval: ${myPlant.wateringIntervalDays} days")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (remainingDays > 0) {
                        showConfirmEarlyWaterDialog = true
                    } else {
                        onWatered()
                        showWateredSnackbar = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
            ) {
                Text("Mark as Watered", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = when {
                    remainingDays > 0 -> "You can water this plant in $remainingDays day(s)."
                    remainingDays == 0L -> "You should water this plant today."
                    else -> "Overdue by ${-remainingDays} day(s)!"
                },
                color = if (remainingDays < 0) Color.Red else Color.Gray
            )
        }

        if (showConfirmEarlyWaterDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmEarlyWaterDialog = false },
                title = { Text("Are you sure?") },
                text = { Text("There are still $remainingDays day(s) until the watering day. Do you want to mark it as watered?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onWatered()
                            showWateredSnackbar = true
                            showConfirmEarlyWaterDialog = false
                        }
                    ) {
                        Text("Yes", color = Color(0xFF0E8D50))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmEarlyWaterDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
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
                            showDeleteSnackbar = true
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
