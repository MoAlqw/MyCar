package com.example.data.repository

import com.example.data.db.dao.VehicleDao
import com.example.data.db.entity.toDomain
import com.example.data.db.entity.toEntity
import com.example.domain.model.Vehicle
import com.example.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehicleRepositoryImpl @Inject constructor(
    private val vehicleDao: VehicleDao
): VehicleRepository {

    override fun observeVehicles(): Flow<List<Vehicle>> {
        return vehicleDao.observeAll().map { vehicles ->
            vehicles.map { it.toDomain() }
        }
    }

    override suspend fun getVehicleById(id: String): Vehicle {
        return vehicleDao.getVehicleById(id).toDomain()
    }

    override suspend fun addVehicle(vehicle: Vehicle) {
        vehicleDao.insert(vehicle.toEntity())
    }

    override suspend fun deleteVehicle(id: String) {
        vehicleDao.deleteById(id)
    }
}