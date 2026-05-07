package com.example.domain.repository.baseline

import kotlinx.coroutines.flow.Flow

interface BaselineVehicleRepository {
    fun getBaselineInspectionId(vehicleId: String): Flow<String?>
    suspend fun setBaseline(vehicleId: String, inspectionId: String)
}