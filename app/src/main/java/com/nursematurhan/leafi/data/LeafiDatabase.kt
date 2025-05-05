package com.nursematurhan.leafi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nursematurhan.leafi.data.model.Plant
import com.nursematurhan.leafi.data.model.WateringHistory

@Database(
    entities = [Plant::class, WateringHistory::class],
    version = 2, // İlk versiyon 1 ise şimdi 2 yapıyoruz
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LeafiDatabase : RoomDatabase() {
    abstract fun wateringDao(): WateringDao

    companion object {
        @Volatile
        private var INSTANCE: LeafiDatabase? = null

        fun getDatabase(context: Context): LeafiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LeafiDatabase::class.java,
                    "leafi_database"
                )
                    .fallbackToDestructiveMigration() // sadece test için, prod'da dikkat!
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
