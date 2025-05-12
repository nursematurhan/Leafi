package com.nursematurhan.leafi.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import androidx.compose.runtime.State
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import java.util.Calendar

class FunFactViewModel : ViewModel() {
    private val _fact = mutableStateOf<FunFact?>(null)
    val fact: State<FunFact?> = _fact

    init {
        loadRandomFact()
    }

    private fun loadRandomFact() {
        Firebase.firestore.collection("funfacts")
            .get()
            .addOnSuccessListener { result ->
                val facts = result.toObjects(FunFact::class.java)
                if (facts.isNotEmpty()) {
                    val calendar = Calendar.getInstance()
                    val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
                    val todayIndex = dayOfYear % facts.size
                    _fact.value = facts[todayIndex]
                }
            }
    }
}

