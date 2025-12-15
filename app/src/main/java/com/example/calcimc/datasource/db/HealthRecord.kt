package com.example.calcimc.datasource.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_records")
data class HealthRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val createdAt: Long,
    val heightCm: Double,
    val weightKg: Double,
    val age: Int,
    val isMale: Boolean,

    val imc: Double,
    val imcClass: String,
    val tmb: Int,
    val idealWeightKg: Double,

    // ✅ NOVO (Opção A)
    val activityFactor: Double,
    val dailyCalories: Int
)
