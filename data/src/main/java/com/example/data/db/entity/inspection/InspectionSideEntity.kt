package com.example.data.db.entity.inspection

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "inspection_sides",
    foreignKeys = [
        ForeignKey(
            entity = InspectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["inspectionId"],
            onDelete = CASCADE
        )
    ]
)
data class InspectionSideEntity(
    @PrimaryKey val id: String,
    val inspectionId: String,
    val side: String,
    val originalPhotoUrl: String,
    val annotatedPhotoUrl: String,
    val hasDamage: Boolean,
    val damageCount: Int
)