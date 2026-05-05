package com.example.domain.model.inspection

data class Inspection(
    val id: String,
    val vehicleId: String,
    val createdAt: String,
    val totalDamageCount: Int,
    val hasAnyDamage: Boolean,
    val sides: List<InspectionSide>
)

data class InspectionSide(
    val id: String,
    val side: CarSide,
    val originalPhotoUrl: String,
    val annotatedPhotoUrl: String,
    val hasDamage: Boolean,
    val damageCount: Int,
    val detections: List<Detection>
)

data class Detection(
    val id: String,
    val label: String,
    val confidence: Float,
    val boundingBox: BoundingBox
)

data class BoundingBox(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float
)