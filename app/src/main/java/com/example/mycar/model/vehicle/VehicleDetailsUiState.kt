package com.example.mycar.model.vehicle

import com.example.domain.model.vehicle.Vehicle
import com.example.mycar.model.inspection.InspectionUi

data class VehicleDetailsUiState(
    val vehicle: Vehicle? = null,
    val inspections: List<InspectionUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)