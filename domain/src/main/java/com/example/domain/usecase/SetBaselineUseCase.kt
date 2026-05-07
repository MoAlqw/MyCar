package com.example.domain.usecase

import com.example.domain.repository.baseline.BaselineVehicleRepository

class SetBaselineUseCase(
    private val baselineVehicleRepository: BaselineVehicleRepository
) {
    suspend operator fun invoke(vehicleId: String, inspectionId: String) {
        baselineVehicleRepository.setBaseline(vehicleId, inspectionId)
    }
}