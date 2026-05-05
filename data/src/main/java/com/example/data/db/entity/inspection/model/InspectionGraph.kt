package com.example.data.db.entity.inspection.model

import com.example.data.db.entity.inspection.DetectionEntity
import com.example.data.db.entity.inspection.InspectionEntity
import com.example.data.db.entity.inspection.InspectionSideEntity
import com.example.domain.model.inspection.Inspection
import java.util.UUID

data class InspectionGraph(
    val inspection: InspectionEntity,
    val sides: List<InspectionSideEntity>,
    val detections: List<DetectionEntity>
)

fun Inspection.toEntityGraph(): InspectionGraph {

    val sideId = UUID.randomUUID().toString()

    val inspectionEntity = InspectionEntity(
        id = id,
        vehicleId = vehicleId,
        createdAt = createdAt,
        totalDamageCount = totalDamageCount,
        hasAnyDamage = hasAnyDamage
    )

    val sideEntities = mutableListOf<InspectionSideEntity>()
    val detectionEntities = mutableListOf<DetectionEntity>()

    sides.forEach { side ->

        val sideEntity = InspectionSideEntity(
            id = sideId,
            inspectionId = id,
            side = side.side.name,
            originalPhotoUrl = side.originalPhotoUrl,
            annotatedPhotoUrl = side.annotatedPhotoUrl,
            hasDamage = side.hasDamage,
            damageCount = side.damageCount
        )

        sideEntities += sideEntity

        side.detections.forEach { det ->
            detectionEntities += DetectionEntity(
                id = UUID.randomUUID().toString(),
                sideId = sideEntity.id,
                label = det.label,
                confidence = det.confidence,
                x1 = det.boundingBox.x1,
                y1 = det.boundingBox.y1,
                x2 = det.boundingBox.x2,
                y2 = det.boundingBox.y2
            )
        }
    }

    return InspectionGraph(
        inspection = inspectionEntity,
        sides = sideEntities,
        detections = detectionEntities
    )
}