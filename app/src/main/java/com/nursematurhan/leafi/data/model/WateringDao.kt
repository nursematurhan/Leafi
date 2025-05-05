package com.nursematurhan.leafi.data

import androidx.room.*
import com.nursematurhan.leafi.data.model.WateringHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface WateringDao {
    @Query("SELECT * FROM watering_history WHERE plantId = :plantId LIMIT 1")
    fun getLastWateringDate(plantId: String): Flow<WateringHistory?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatering(watering: WateringHistory)
}
