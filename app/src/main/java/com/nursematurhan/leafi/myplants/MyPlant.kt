package com.nursematurhan.leafi.myplants

data class MyPlant(
    val plantId: String = "",
    val userId: String = "",
    val plantName: String = "",
    val plantImageUrl: String = "",
    val wateringIntervalDays: Int = 3,
    val lastWateredDate: Long = 0L
)
