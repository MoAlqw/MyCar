package com.example.domain.usecase

import com.example.domain.comparator.InspectionComparator
import com.example.domain.model.comparison.InspectionComparison
import com.example.domain.repository.inspection.InspectionRepository

class CompareInspectionUseCase(
    private val inspectionRepository: InspectionRepository,
    private val comparator: InspectionComparator
) {

    suspend operator fun invoke(
        baselineId: String,
        currentInspectionId: String
    ): InspectionComparison {

        val baseline = inspectionRepository.getInspection(baselineId)
        val current = inspectionRepository.getInspection(currentInspectionId)

        return comparator.compare(
            baseline = baseline,
            current = current
        )
    }
}