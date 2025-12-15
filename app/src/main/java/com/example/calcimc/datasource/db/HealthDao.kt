package com.example.calcimc.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: HealthRecord)

    @Query("SELECT * FROM health_records ORDER BY createdAt DESC")
    fun getAll(): Flow<List<HealthRecord>>

    @Query("SELECT * FROM health_records WHERE id = :id")
    fun getById(id: Long): Flow<HealthRecord?>
}
