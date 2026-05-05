package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.db.dao.InspectionDao
import com.example.data.db.dao.VehicleDao
import com.example.data.db.entity.VehicleEntity
import com.example.data.db.entity.inspection.DetectionEntity
import com.example.data.db.entity.inspection.InspectionEntity
import com.example.data.db.entity.inspection.InspectionSideEntity

@Database(
    entities = [
        VehicleEntity::class,
        InspectionEntity::class,
        InspectionSideEntity::class,
        DetectionEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
    abstract fun inspectionDao(): InspectionDao
}