package com.nursematurhan.leafi.ui.plants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nursematurhan.leafi.data.PlantRepository
import com.nursematurhan.leafi.ui.myplants.MyPlant
import com.nursematurhan.leafi.data.model.Plant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlantListViewModel : ViewModel() {

    private val _plants = MutableStateFlow<List<Plant>>(emptyList())
    val plants: StateFlow<List<Plant>> = _plants

    init {
        fetchPlants()
    }

    private fun fetchPlants() {
        viewModelScope.launch {
            PlantRepository.getPlants { result ->
                _plants.value = result
            }
        }
    }

    fun getPlantById(plantId: String): Plant? {
        return _plants.value.firstOrNull { it.id == plantId }
    }

    fun addToMyPlants(plant: Plant, interval: Int, date: Long) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val myPlant = MyPlant(
            plantId = plant.id,
            userId = userId,
            plantName = plant.name,
            plantImageUrl = plant.imageUrl,
            wateringIntervalDays = interval,
            lastWateredDate = date
        )

        FirebaseFirestore.getInstance()
            .collection("MyPlants")
            .document(userId)
            .collection("Plants")
            .document(plant.id)
            .set(myPlant)
    }
}
