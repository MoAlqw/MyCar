package com.example.domain.usecase

import com.example.domain.repository.VehicleRepository

class DeleteVehicleUseCase(
    private val vehicleRepository: VehicleRepository
) {
    suspend operator fun invoke(id: String) {
        vehicleRepository.deleteVehicle(id)
    }
}