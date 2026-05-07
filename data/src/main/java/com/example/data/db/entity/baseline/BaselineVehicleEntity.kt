package com.example.data.db.entity.baseline

import androidx.room.Entity

@Entity(
    tableName = "vehicle_baseline",
    primaryKeys = ["vehicleId"]
)
data class BaselineVehicleEntity(
    val vehicleId: String,
    val inspectionId: String
)