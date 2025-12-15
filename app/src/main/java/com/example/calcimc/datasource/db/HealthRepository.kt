package com.example.calcimc.datasource.db

import kotlinx.coroutines.flow.Flow

class HealthRepository(
    private val dao: HealthDao
) {
    suspend fun insert(record: HealthRecord) =
        dao.insert(record)

    fun getAll(): Flow<List<HealthRecord>> =
        dao.getAll()

    fun getById(id: Long): Flow<HealthRecord?> =
        dao.getById(id)
}
