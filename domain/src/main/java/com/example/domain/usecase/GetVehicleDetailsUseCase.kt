package com.example.domain.usecase

import com.example.domain.model.vehicle.Vehicle
import com.example.domain.repository.vehicle.VehicleRepository

class GetVehicleDetailsUseCase(
    private val vehicleRepository: VehicleRepository
) {

    suspend operator fun invoke(vehicleId: String): Vehicle {
        return vehicleRepository.getVehicleById(vehicleId)
    }
}