package com.example.domain.usecase

import com.example.domain.model.inspection.Inspection
import com.example.domain.repository.InspectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetAllInspectionsOfVehicleUseCase(
    private val inspectionRepository: InspectionRepository
) {
    operator fun invoke(vehicleId: String): Flow<List<Inspection>> =
        inspectionRepository.getInspections(vehicleId)
            .flowOn(Dispatchers.IO)
}