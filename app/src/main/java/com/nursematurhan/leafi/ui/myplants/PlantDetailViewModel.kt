package com.nursematurhan.leafi.ui.myplants

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nursematurhan.leafi.ui.data.LeafiDatabase
import com.nursematurhan.leafi.ui.data.model.WateringHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class PlantDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = LeafiDatabase.getDatabase(application).wateringDao()

    private val _lastWatered = MutableStateFlow<Date?>(null)
    val lastWatered: StateFlow<Date?> = _lastWatered

    fun loadWateringDate(plantId: String) {
        viewModelScope.launch {
            dao.getLastWateringDate(plantId).collect { history ->
                _lastWatered.value = history?.lastWateredDate
            }
        }
    }

    fun saveWateringDate(plantId: String) {
        viewModelScope.launch {
            val now = Date()
            dao.insertWatering(WateringHistory(plantId = plantId, lastWateredDate = now))
            _lastWatered.value = now
        }
    }
}
