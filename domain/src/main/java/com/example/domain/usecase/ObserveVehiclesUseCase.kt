package com.example.domain.usecase

import com.example.domain.model.Vehicle
import com.example.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow

class ObserveVehiclesUseCase(
    private val vehicleRepository: VehicleRepository
) {
    operator fun invoke(): Flow<List<Vehicle>> {
        return vehicleRepository.observeVehicles()
    }
}