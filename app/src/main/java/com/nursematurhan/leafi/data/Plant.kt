package com.nursematurhan.leafi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Plant(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val wateringInterval: Int = 0
)

