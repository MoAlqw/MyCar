package com.example.data.db.entity.inspection

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inspections")
data class InspectionEntity(
    @PrimaryKey val id: String,
    val vehicleId: String,
    val createdAt: String,
    val totalDamageCount: Int,
    val hasAnyDamage: Boolean
)