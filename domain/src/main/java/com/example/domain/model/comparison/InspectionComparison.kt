package com.example.domain.model.comparison

data class InspectionComparison(
    val baselineInspectionId: String,
    val currentInspectionId: String,
    val baselineDate: String,
    val currentInspectionDate: String,
    val baselineInspectionPhoto: List<String>,
    val currentInspectionPhoto: List<String>,
    val changes: List<DamageComparison>,
)

fun InspectionComparison.addedCount(): Int {
    return changes.count {
        it.type == DamageChangeType.ADDED
    }
}

fun InspectionComparison.removedCount(): Int {
    return changes.count {
        it.type == DamageChangeType.REMOVED
    }
}