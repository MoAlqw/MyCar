package com.example.mycar.model.inspection

import com.example.domain.model.inspection.Inspection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class InspectionUi(
    val id: String,
    val displayDate: String,
    val damageStatus: Int
)

fun Inspection.toUi(): InspectionUi {
    val dateTime = LocalDateTime.parse(createdAt)
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)

    return InspectionUi(
        id = id,
        displayDate = dateTime.format(formatter),
        damageStatus = totalDamageCount
    )
}