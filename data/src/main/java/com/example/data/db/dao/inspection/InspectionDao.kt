package com.example.data.db.dao.inspection

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.db.entity.inspection.DetectionEntity
import com.example.data.db.entity.inspection.InspectionEntity
import com.example.data.db.entity.inspection.InspectionSideEntity
import com.example.data.db.entity.inspection.model.InspectionFull
import kotlinx.coroutines.flow.Flow

@Dao
interface InspectionDao {

    @Transaction
    @Query("SELECT * FROM inspections WHERE id = :id")
    suspend fun getInspection(id: String): InspectionFull

    @Transaction
    @Query("SELECT * FROM inspections WHERE vehicleId = :vehicleId")
    fun getInspectionsByVehicleId(vehicleId: String): Flow<List<InspectionFull>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspection(entity: InspectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSide(entity: InspectionSideEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetections(list: List<DetectionEntity>)
}