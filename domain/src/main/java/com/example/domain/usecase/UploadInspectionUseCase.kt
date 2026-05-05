package com.example.domain.usecase

import com.example.domain.model.inspection.CarSide
import com.example.domain.model.inspection.Inspection
import com.example.domain.repository.InspectionRepository

class UploadInspectionUseCase(
    private val inspectionRepository: InspectionRepository
) {
    suspend operator fun invoke(
        vehicleId: String,
        photos: Map<CarSide, String>
    ): Inspection {
        return inspectionRepository.createInspection(vehicleId, photos)
    }
}