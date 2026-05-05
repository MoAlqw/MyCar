package com.example.data.db.entity.inspection

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.example.domain.model.inspection.BoundingBox
import com.example.domain.model.inspection.Detection

@Entity(
    tableName = "detections",
    foreignKeys = [
        ForeignKey(
            entity = InspectionSideEntity::class,
            parentColumns = ["id"],
            childColumns = ["sideId"],
            onDelete = CASCADE
        )
    ]
)
data class DetectionEntity(
    @PrimaryKey val id: String,
    val sideId: String,
    val label: String,
    val confidence: Float,
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float
)

fun DetectionEntity.toDomain(): Detection {
    return Detection(
        id = id,
        label = label,
        confidence = confidence,
        boundingBox = BoundingBox(
            x1 = x1,
            y1 = y1,
            x2 = x2,
            y2 = y2
        )
    )
}