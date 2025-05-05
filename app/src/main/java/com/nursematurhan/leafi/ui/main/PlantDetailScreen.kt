package com.nursematurhan.leafi.ui.main

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.nursematurhan.leafi.data.model.Plant
import com.nursematurhan.leafi.ui.components.PageWithBackButton
import java.text.SimpleDateFormat
import androidx.navigation.NavHostController
import java.util.*

@Composable
fun PlantDetailScreen(plant: Plant, navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: PlantDetailViewModel = viewModel(
        factory = object : ViewModelProvider.AndroidViewModelFactory(context.applicationContext as Application) {}
    )

    val lastWatered by viewModel.lastWatered.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWateringDate(plant.id)
    }

    PageWithBackButton(
        title = plant.name,
        onBackClick = { navController.popBackStack() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(plant.imageUrl),
                contentDescription = plant.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = plant.description)

            Spacer(modifier = Modifier.height(12.dp))

            lastWatered?.let {
                Text(
                    text = "Son sulama: ${
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(it)
                    }"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { viewModel.saveWateringDate(plant.id) }) {
                Text("💧 Suladım")
            }
        }
    }
}
