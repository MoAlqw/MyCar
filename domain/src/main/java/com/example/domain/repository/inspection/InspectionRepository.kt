package com.example.domain.repository.inspection

import com.example.domain.model.inspection.CarSide
import com.example.domain.model.inspection.Inspection
import kotlinx.coroutines.flow.Flow

interface InspectionRepository {
    suspend fun createInspection(vehicleId: String, photos: Map<CarSide, String>): Inspection
    suspend fun getInspection(id: String): Inspection
    fun getInspections(vehicleId: String): Flow<List<Inspection>>
    suspend fun syncInspections(vehicleId: String)
}