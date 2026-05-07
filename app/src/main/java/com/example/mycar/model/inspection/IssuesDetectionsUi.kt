package com.example.mycar.model.inspection

import com.example.mycar.R

data class IssuesDetectionsUi(
    val name: String,
    val levelOfSeverity: Int,
    val confidence: Int
)

fun InspectionOfVehicleUi.toIssuesDetectionsUi(): List<IssuesDetectionsUi> {
    val issuesDetectionsUi = mutableListOf<IssuesDetectionsUi>()
    for (detection in detections) {
        issuesDetectionsUi.add(
            IssuesDetectionsUi(
                name = detection,
                levelOfSeverity = if (detection.contains("Dent", true)) R.string.severity_level_2 else R.string.severity_level_1,
                confidence = 98
            )
        )
    }
    return issuesDetectionsUi
}