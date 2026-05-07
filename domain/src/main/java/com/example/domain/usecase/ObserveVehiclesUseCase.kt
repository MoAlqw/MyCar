package com.example.domain.usecase

import com.example.domain.model.vehicle.Vehicle
import com.example.domain.repository.vehicle.VehicleRepository
import kotlinx.coroutines.flow.Flow

class ObserveVehiclesUseCase(
    private val vehicleRepository: VehicleRepository
) {
    operator fun invoke(): Flow<List<Vehicle>> {
        return vehicleRepository.observeVehicles()
    }
}