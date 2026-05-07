package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.db.dao.baseline.BaselineVehicleDao
import com.example.data.db.dao.inspection.InspectionDao
import com.example.data.db.dao.vehicle.VehicleDao
import com.example.data.db.entity.VehicleEntity
import com.example.data.db.entity.baseline.BaselineVehicleEntity
import com.example.data.db.entity.inspection.DetectionEntity
import com.example.data.db.entity.inspection.InspectionEntity
import com.example.data.db.entity.inspection.InspectionSideEntity

@Database(
    entities = [
        VehicleEntity::class,
        InspectionEntity::class,
        InspectionSideEntity::class,
        DetectionEntity::class,
        BaselineVehicleEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
    abstract fun inspectionDao(): InspectionDao
    abstract fun baselineDao(): BaselineVehicleDao
}