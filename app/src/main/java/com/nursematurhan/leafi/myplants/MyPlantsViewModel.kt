package com.nursematurhan.leafi.myplants

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MyPlantsViewModel : ViewModel() {

    private val _myPlants = MutableStateFlow<List<MyPlant>>(emptyList())
    val myPlants: StateFlow<List<MyPlant>> = _myPlants

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    init {
        fetchMyPlants()
    }

    fun fetchMyPlants() {
        if (userId == null) return
        firestore
            .collection("MyPlants")
            .document(userId)
            .collection("Plants")
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { it.toObject(MyPlant::class.java) }
                _myPlants.value = list
            }
    }

    fun markAsWatered(plantId: String) {
        if (userId == null) return
        val now = System.currentTimeMillis()
        firestore
            .collection("MyPlants")
            .document(userId)
            .collection("Plants")
            .document(plantId)
            .update("lastWateredDate", now)
            .addOnSuccessListener {
                fetchMyPlants()
            }
    }

    fun deletePlant(plantId: String, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        if (userId == null) return
        firestore
            .collection("MyPlants")
            .document(userId)
            .collection("Plants")
            .document(plantId)
            .delete()
            .addOnSuccessListener {
                fetchMyPlants()
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}
