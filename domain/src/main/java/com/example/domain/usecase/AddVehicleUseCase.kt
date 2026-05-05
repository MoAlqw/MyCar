package com.example.domain.usecase

import com.example.domain.model.Vehicle
import com.example.domain.repository.VehicleRepository

class AddVehicleUseCase(
    private val vehicleRepository: VehicleRepository
) {
    suspend operator fun invoke(vehicle: Vehicle) {
        vehicleRepository.addVehicle(vehicle)
    }
}