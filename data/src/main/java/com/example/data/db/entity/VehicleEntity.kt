package com.example.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.vehicle.Vehicle

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey val id: String,
    val make: String,
    val model: String,
    val year: Int,
    val plate: String,
    val vin: String
)

fun VehicleEntity.toDomain(): Vehicle = Vehicle(
    id = id,
    make = make,
    model = model,
    year = year,
    plate = plate,
    vin = vin
)

fun Vehicle.toEntity(): VehicleEntity = VehicleEntity(
    id = id,
    make = make,
    model = model,
    year = year,
    plate = plate,
    vin = vin
)