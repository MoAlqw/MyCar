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

    val progressText: String
        get() = "STEP ${currentStepIndex + 1} OF ${steps.size}"

    val isLastStep: Boolean
        get() = currentStepIndex == steps.lastIndex

    val imageOfCurrentStep: Int
        get() = when (currentStep.side) {
            CarSide.FRONT, CarSide.REAR -> {
                R.drawable.car_front
            }
            CarSide.LEFT -> R.drawable.car_side
            CarSide.RIGHT -> R.drawable.car_side
        }
}

fun InspectionCaptureUiState.toOverlay() = when (this.currentSide) {
    CarSide.FRONT, CarSide.REAR -> OverlayConfig(R.drawable.car_front)
    CarSide.LEFT -> OverlayConfig(R.drawable.car_side)
    CarSide.RIGHT -> OverlayConfig(R.drawable.car_side, scaleX = -1f)
}