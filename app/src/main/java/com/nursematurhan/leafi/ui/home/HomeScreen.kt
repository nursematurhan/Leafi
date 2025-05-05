package com.nursematurhan.leafi.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nursematurhan.leafi.R

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Başlık
        Text(
            text = "Welcome to Leafi 🌿",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Discover personalized plant care tips and manage your own garden effortlessly.",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        VerticalSuggestions()
    }
}

@Composable
fun VerticalSuggestions() {
    val suggestions = listOf(
        "Garden Plants" to R.drawable.plant1,
        "Indoor Plants" to R.drawable.plant2,
        "Herbs" to R.drawable.plant3
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        suggestions.forEach { (title, imageRes) ->
            SuggestionCard(title = title, imageRes = imageRes)
        }
    }
}

@Composable
fun SuggestionCard(title: String, imageRes: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
