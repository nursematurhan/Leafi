package com.nursematurhan.leafi.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nursematurhan.leafi.data.PlantRepository
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
}
