package com.example.mycar.model.inspection

import com.example.domain.model.inspection.CarSide
import com.example.mycar.R

data class InspectionCaptureUiState(
    val inspectionId: String? = null,
    val vehicleId: String? = null,
    val currentStepIndex: Int = 0,
    val steps: List<InspectionPhotoStep> = listOf(
        InspectionPhotoStep(CarSide.FRONT),
        InspectionPhotoStep(CarSide.REAR),
        InspectionPhotoStep(CarSide.LEFT),
        InspectionPhotoStep(CarSide.RIGHT)
    ),
    val isUploading: Boolean = false
) {
    val currentStep: InspectionPhotoStep
        get() = steps[currentStepIndex]

    val currentSide: CarSide
        get() = currentStep.side

    val progressText: Int
        get() = R.string.step_of_state_inspection_vehicle


    val isLastStep: Boolean
        get() = currentStepIndex == steps.lastIndex
}

fun InspectionCaptureUiState.toOverlay() = when (this.currentSide) {
    CarSide.FRONT, CarSide.REAR -> OverlayConfig(R.drawable.car_front)
    CarSide.LEFT -> OverlayConfig(R.drawable.car_side)
    CarSide.RIGHT -> OverlayConfig(R.drawable.car_side, scaleX = -1f)
}