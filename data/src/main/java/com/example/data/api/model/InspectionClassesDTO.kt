package com.example.data.api.model

import com.example.domain.model.inspection.BoundingBox
import com.example.domain.model.inspection.Detection
import com.example.domain.model.inspection.Inspection
import com.example.domain.model.inspection.InspectionSide
import com.example.domain.model.inspection.toCarSide

data class InspectionResponseDto(
    val inspection_id: String,
    val vehicle_id: String,
    val created_at: String,
    val total_damage_count: Int,
    val has_any_damage: Boolean,
    val sides: List<InspectionSideDto>
)

data class InspectionSideDto(
    val id: String,
    val side: String,
    val original_photo_url: String,
    val annotated_photo_url: String,
    val has_damage: Boolean,
    val damage_count: Int,
    val damage_detections: List<DetectionDto>
) {
    companion object {
        const val FRONT = "front"
        const val REAR = "rear"
        const val LEFT = "left"
        const val RIGHT = "right"
    }
}

data class DetectionDto(
    val id: String,
    val label: String,
    val confidence: Double,
    val bbox: BoundingBoxDto
)

data class BoundingBoxDto(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float
)

fun InspectionResponseDto.toDomain(): Inspection {
    return Inspection(
        id = inspection_id,
        vehicleId = vehicle_id,
        createdAt = created_at,
        totalDamageCount = total_damage_count,
        hasAnyDamage = has_any_damage,
        sides = sides.map { it.toDomain() }
    )
}

fun InspectionSideDto.toDomain(): InspectionSide {
    return InspectionSide(
        id = id,
        side = side.toCarSide(),
        originalPhotoUrl = original_photo_url,
        annotatedPhotoUrl = annotated_photo_url,
        hasDamage = has_damage,
        damageCount = damage_count,
        detections = damage_detections.map { it.toDomain() }
    )
}

fun DetectionDto.toDomain(): Detection {
    return Detection(
        id = id,
        label = label,
        confidence = confidence.toFloat(),
        boundingBox = bbox.toDomain()
    )
}

fun BoundingBoxDto.toDomain(): BoundingBox {
    return BoundingBox(
        x1 = x1,
        y1 = y1,
        x2 = x2,
        y2 = y2
    )
}