package com.example.data.db.entity.inspection.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.data.db.entity.inspection.DetectionEntity
import com.example.data.db.entity.inspection.InspectionEntity
import com.example.data.db.entity.inspection.InspectionSideEntity
import com.example.data.db.entity.inspection.toDomain
import com.example.domain.model.inspection.Inspection
import com.example.domain.model.inspection.InspectionSide
import com.example.domain.model.inspection.toCarSide

data class InspectionFull(
    @Embedded val inspection: InspectionEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "inspectionId",
        entity = InspectionSideEntity::class
    )
    val sides: List<InspectionSideWithDetections>
)

data class InspectionSideWithDetections(
    @Embedded val side: InspectionSideEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "sideId"
    )
    val detections: List<DetectionEntity>
)

fun InspectionFull.toDomain(): Inspection {
    return Inspection(
        id = inspection.id,
        vehicleId = inspection.vehicleId,
        createdAt = inspection.createdAt,
        totalDamageCount = inspection.totalDamageCount,
        hasAnyDamage = inspection.hasAnyDamage,
        sides = sides.map { it.toDomain() }
    )
}

fun InspectionSideWithDetections.toDomain(): InspectionSide {
    return InspectionSide(
        id = side.id,
        side = side.side.toCarSide(),
        originalPhotoUrl = side.originalPhotoUrl,
        annotatedPhotoUrl = side.annotatedPhotoUrl,
        hasDamage = side.hasDamage,
        damageCount = side.damageCount,
        detections = detections.map { it.toDomain() }
    )
}