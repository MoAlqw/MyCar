package com.example.domain.usecase

import com.example.domain.repository.baseline.BaselineVehicleRepository
import com.example.domain.repository.inspection.InspectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetBaselineUseCase(
    private val baselineVehicleRepository: BaselineVehicleRepository,
    private val inspectionRepository: InspectionRepository
) {
    operator fun invoke(vehicleId: String): Flow<String?> {
        return combine(
            baselineVehicleRepository.getBaselineInspectionId(vehicleId),
            inspectionRepository.getInspections(vehicleId)
        ) { baselineId, inspections ->

            when {
                baselineId != null -> baselineId

                inspections.isNotEmpty() -> {
                    inspections.minByOrNull { it.createdAt }?.id
                }

                else -> null
            }
        }
    }
}