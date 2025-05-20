package com.nursematurhan.leafi.plants

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object PlantRepository {

    fun getPlants(callback: (List<Plant>) -> Unit) {
        Firebase.firestore.collection("plants")
            .get()
            .addOnSuccessListener { result ->
                val plantList = result.mapNotNull { it.toObject(Plant::class.java) }
                callback(plantList)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }
    fun addPlant(plant: Plant, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        Firebase.firestore.collection("plants")
            .add(plant)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

}
