package com.nursematurhan.leafi.ui.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "watering_history")
data class WateringHistory(
    @PrimaryKey val plantId: String,
    val lastWateredDate: Date
)
