package com.example.domain.usecase

import com.example.domain.repository.InspectionRepository

class SyncInspectionsUseCase(
    private val inspectionRepository: InspectionRepository
) {
    suspend operator fun invoke(vehicleId: String) {
        inspectionRepository.syncInspections(vehicleId)
    }
}