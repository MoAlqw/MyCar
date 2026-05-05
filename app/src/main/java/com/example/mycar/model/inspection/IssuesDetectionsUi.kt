package com.example.mycar.model.inspection

data class IssuesDetectionsUi(
    val name: String,
    val levelOfSeverity: String,
    val confidence: Int
)

fun InspectionOfVehicleUi.toIssuesDetectionsUi(): List<IssuesDetectionsUi> {
    val issuesDetectionsUi = mutableListOf<IssuesDetectionsUi>()
    for (detection in detections) {
        issuesDetectionsUi.add(
            IssuesDetectionsUi(
                name = detection,
                levelOfSeverity = if (detection.contains("Dent", true)) "Severity: Level 2" else "Severity: Level 1",
                confidence = 98
            )
        )
    }
    return issuesDetectionsUi
}