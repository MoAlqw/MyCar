package com.example.mycar.model.inspection

import com.example.domain.model.inspection.Inspection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class InspectionUi(
    val id: String,
    val vehicleId: String,
    val displayDate: String,
    val damageStatus: Int,
    val addedIssues: Int = 0,
    val removedIssues: Int = 0,
    val isBaseline: Boolean = false
)

fun Inspection.toUi(): InspectionUi {
    val dateTime = LocalDateTime.parse(createdAt)
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy ", Locale.ENGLISH)
    val statusString = if (totalDamageCount > 0) "• Minor Issues" else "• Passed"

    return InspectionUi(
        id = id,
        vehicleId = vehicleId,
        displayDate = dateTime.format(formatter) + statusString,
        damageStatus = totalDamageCount
    )
}