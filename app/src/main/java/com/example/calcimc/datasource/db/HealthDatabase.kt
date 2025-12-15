package com.example.calcimc.datasource.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [HealthRecord::class],
    version = 2, // ✅ era 1
    exportSchema = false
)
abstract class HealthDatabase : RoomDatabase() {

    abstract fun healthDao(): HealthDao

    companion object {
        @Volatile
        private var INSTANCE: HealthDatabase? = null

        fun getInstance(context: Context): HealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthDatabase::class.java,
                    "health_db"
                )
                    .fallbackToDestructiveMigration() // ✅ recria se mudar schema
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
