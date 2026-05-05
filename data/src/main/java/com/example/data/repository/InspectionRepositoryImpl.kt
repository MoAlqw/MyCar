package com.example.data.repository

import androidx.room.withTransaction
import com.example.data.api.InspectionApi
import com.example.data.api.model.toDomain
import com.example.data.db.AppDatabase
import com.example.data.db.dao.InspectionDao
import com.example.data.db.entity.inspection.DetectionEntity
import com.example.data.db.entity.inspection.InspectionEntity
import com.example.data.db.entity.inspection.InspectionSideEntity
import com.example.data.db.entity.inspection.model.toDomain
import com.example.domain.model.inspection.CarSide
import com.example.domain.model.inspection.Inspection
import com.example.domain.repository.InspectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InspectionRepositoryImpl @Inject constructor(
    private val api: InspectionApi,
    private val db: AppDatabase,
    private val dao: InspectionDao
) : InspectionRepository {

    override suspend fun createInspection(
        vehicleId: String,
        photos: Map<CarSide, String>
    ): Inspection {

        val response = api.analyzeInspection(
            vehicleId = vehicleId.toRequestBody("text/plain".toMediaType()),
            frontPhoto = createPart("front_photo", photos[CarSide.FRONT]!!),
            rearPhoto = createPart("rear_photo", photos[CarSide.REAR]!!),
            leftPhoto = createPart("left_photo", photos[CarSide.LEFT]!!),
            rightPhoto = createPart("right_photo", photos[CarSide.RIGHT]!!)
        )

        if (!response.isSuccessful) {
            val error = response.errorBody()?.string()
            throw IllegalStateException("API error ${response.code()} $error")
        }

        val domain = response.body()?.toDomain()
            ?: throw IllegalStateException("Empty response body")

        db.withTransaction {
            saveInspectionToDb(domain)
        }

        return domain
    }

    override suspend fun getInspection(id: String): Inspection {
        return dao.getInspection(id).toDomain()
    }

    override fun getInspections(vehicleId: String): Flow<List<Inspection>> {
        return dao.getInspectionsByVehicleId(vehicleId).map { inspections ->
            inspections.map { it.toDomain() }
        }
    }

    override suspend fun syncInspections(vehicleId: String) {
        val response = api.getVehicleInspections(vehicleId)

        if (!response.isSuccessful) {
            return
        }

        val remoteInspections = response.body()
            ?: throw IllegalStateException("Empty response body")

        db.withTransaction {
            remoteInspections.forEach { dto ->
                val domain = dto.toDomain()
                saveInspectionToDb(domain)
            }
        }
    }

    private suspend fun saveInspectionToDb(inspection: Inspection) {

        val inspectionEntity = InspectionEntity(
            id = inspection.id,
            vehicleId = inspection.vehicleId,
            createdAt = inspection.createdAt,
            totalDamageCount = inspection.totalDamageCount,
            hasAnyDamage = inspection.hasAnyDamage
        )

        dao.insertInspection(inspectionEntity)

        inspection.sides.forEach { side ->

            val sideEntity = InspectionSideEntity(
                id = side.id,
                inspectionId = inspection.id,
                side = side.side.name,
                originalPhotoUrl = side.originalPhotoUrl,
                annotatedPhotoUrl = side.annotatedPhotoUrl,
                hasDamage = side.hasDamage,
                damageCount = side.damageCount
            )

            dao.insertSide(sideEntity)

            val detections = side.detections.map { det ->
                DetectionEntity(
                    id = det.id,
                    sideId = side.id,
                    label = det.label,
                    confidence = det.confidence,
                    x1 = det.boundingBox.x1,
                    y1 = det.boundingBox.y1,
                    x2 = det.boundingBox.x2,
                    y2 = det.boundingBox.y2
                )
            }

            if (detections.isNotEmpty()) {
                dao.insertDetections(detections)
            }
        }
    }

    private fun createPart(partName: String, path: String): MultipartBody.Part {
        val file = File(path)
        val requestBody = file.asRequestBody("image/jpeg".toMediaType())
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }
}
