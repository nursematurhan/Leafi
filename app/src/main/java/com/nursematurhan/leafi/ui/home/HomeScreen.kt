package com.nursematurhan.leafi.ui.home

import WeatherViewModel
import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nursematurhan.leafi.util.getLastKnownLocation
import com.nursematurhan.leafi.util.hasLocationPermission
import com.nursematurhan.leafi.util.requestLocationPermission

@Composable
fun HomeScreen(navController: NavController) {
    val weatherViewModel: WeatherViewModel = viewModel()
    val weather = weatherViewModel.weather.value
    val context = LocalContext.current
    val activity = context as? Activity
    var locationFetched by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (hasLocationPermission(context)) {
            getLastKnownLocation(context) { lat, lon ->
                weatherViewModel.loadWeatherByCoords(lat, lon, "63d33eafa64a5619dcafe8b998360639")
                locationFetched = true
            }
        } else {
            activity?.let {
                requestLocationPermission(it)
            }
        }

        if (!locationFetched) {
            weatherViewModel.loadWeather("Istanbul", "63d33eafa64a5619dcafe8b998360639")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Welcome to Leafi 🌿",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        )

        Text(
            text = "Discover personalized plant care tips and manage your own garden effortlessly.",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
        )

        weather?.let {
            WeatherSection(
                temperature = "${it.main.temp}°C",
                humidity = "${it.main.humidity}%",
                condition = it.weather.firstOrNull()?.main ?: "Unknown"
            )
        } ?: Text("Loading weather data...")

        DidYouKnowCard()

        HowItWorksSection()

        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Start Now", color = Color.White)
        }
    }
}

@Composable
fun WeatherSection(temperature: String, humidity: String, condition: String) {
    Text("Outdoor weather", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WeatherCard("Temperature", temperature, "☀️")
        WeatherCard("Humidity", humidity, "💧")
        WeatherCard("Condition", condition, "🌿")
    }
}

@Composable
fun WeatherCard(label: String, value: String, emoji: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(100.dp)
            .height(80.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = emoji, fontSize = 20.sp)
            Text(text = value, fontWeight = FontWeight.Bold)
            Text(text = label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun DidYouKnowCard(viewModel: FunFactViewModel = viewModel()) {
    val fact = viewModel.fact.value

    fact?.let {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AsyncImage(
                    model = it.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "🌱 Did you know?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(it.text, style = MaterialTheme.typography.bodyMedium)
            }
        }
    } ?: Text("Loading fun fact...")
}

@Composable
fun HowItWorksSection() {
    Text(
        text = "How it works?",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(bottom = 8.dp)
    )

    val steps = listOf(
        "🌿 Add your plants and set watering schedules.",
        "📋 Track and manage your plants with reminders.",
        "🔔 Get notified when it’s time to water!"
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        steps.forEachIndexed { index, step ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .size(32.dp)
                            .padding(end = 12.dp)
                    )
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}