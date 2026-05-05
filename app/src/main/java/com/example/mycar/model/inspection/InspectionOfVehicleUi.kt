package com.example.mycar.model.inspection

import com.example.domain.model.inspection.Inspection

data class InspectionOfVehicleUi(
    val totalDamageCount: Int,
    val confidence: Int,
    val detections: List<String>,
    val sides: List<InspectionSideUi>
)

data class InspectionSideUi(
    val side: String,
    val annotatedPhotoUrl: String
)

fun Inspection.toInspectionOfVehicleUi(): InspectionOfVehicleUi {
    var sum = 0.0
    var count = 0
    val detections = mutableListOf<String>()
    for (side in sides) {
        for (detection in side.detections) {
            sum += detection.confidence
            count++
            detections.add(detection.label)
        }
    }

    val avg = if (count > 0) (sum / count) else 100

    return InspectionOfVehicleUi(
        totalDamageCount = totalDamageCount,
        confidence = avg.toInt(),
        detections = detections,
        sides = sides.map {
            InspectionSideUi(
                side = it.side.name,
                annotatedPhotoUrl = it.annotatedPhotoUrl
            )
        }
    )
}