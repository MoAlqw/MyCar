package com.example.mycar.model.inspection

import com.example.domain.model.inspection.CarSide

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
}