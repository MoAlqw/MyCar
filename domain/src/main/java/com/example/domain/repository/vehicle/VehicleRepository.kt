package com.example.domain.repository.vehicle

import com.example.domain.model.vehicle.Vehicle
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    suspend fun getVehicleById(id: String): Vehicle
    fun observeVehicles(): Flow<List<Vehicle>>
    suspend fun addVehicle(vehicle: Vehicle)
    suspend fun deleteVehicle(id: String)
}