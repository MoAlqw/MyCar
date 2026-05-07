package com.example.domain.usecase

import com.example.domain.model.vehicle.Vehicle
import com.example.domain.repository.vehicle.VehicleRepository

class AddVehicleUseCase(
    private val vehicleRepository: VehicleRepository
) {
    suspend operator fun invoke(vehicle: Vehicle) {
        vehicleRepository.addVehicle(vehicle)
    }
}