package com.example.data.repository

import com.example.data.db.dao.baseline.BaselineVehicleDao
import com.example.data.db.entity.baseline.BaselineVehicleEntity
import com.example.domain.repository.baseline.BaselineVehicleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaselineVehicleRepositoryImpl @Inject constructor(
    private val baselineVehicleDao: BaselineVehicleDao
): BaselineVehicleRepository {

    override fun getBaselineInspectionId(vehicleId: String): Flow<String?> {
        return baselineVehicleDao.getBaselineInspectionId(vehicleId)
    }

    override suspend fun setBaseline(vehicleId: String, inspectionId: String) {
        val baseline = BaselineVehicleEntity(vehicleId, inspectionId)
        baselineVehicleDao.setBaseline(baseline)
    }
}