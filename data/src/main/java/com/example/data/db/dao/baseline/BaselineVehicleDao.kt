package com.example.data.db.dao.baseline

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.db.entity.baseline.BaselineVehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BaselineVehicleDao {
    @Query("SELECT inspectionId FROM vehicle_baseline WHERE vehicleId = :vehicleId")
    fun getBaselineInspectionId(vehicleId: String): Flow<String?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setBaseline(entity: BaselineVehicleEntity)
}