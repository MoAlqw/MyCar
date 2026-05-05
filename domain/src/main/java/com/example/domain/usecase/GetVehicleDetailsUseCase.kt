package com.example.domain.usecase

import com.example.domain.model.Vehicle
import com.example.domain.repository.VehicleRepository

class GetVehicleDetailsUseCase(
    private val vehicleRepository: VehicleRepository
) {

    suspend operator fun invoke(vehicleId: String): Vehicle {
        return vehicleRepository.getVehicleById(vehicleId)
    }
}