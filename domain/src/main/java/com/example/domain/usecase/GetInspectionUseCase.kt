package com.example.domain.usecase

import com.example.domain.model.inspection.Inspection
import com.example.domain.repository.InspectionRepository

class GetInspectionUseCase(
    private val inspectionRepository: InspectionRepository
) {
    suspend operator fun invoke(id: String): Inspection {
        return inspectionRepository.getInspection(id)
    }
}