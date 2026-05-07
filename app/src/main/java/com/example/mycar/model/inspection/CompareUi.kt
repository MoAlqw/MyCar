package com.example.mycar.model.inspection

import com.example.domain.model.comparison.DamageChangeType
import com.example.domain.model.comparison.DamageComparison
import com.example.domain.model.comparison.InspectionComparison
import com.example.domain.model.inspection.CarSide
import com.example.domain.model.inspection.nameSide
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.abs

data class CompareUi(
    val baselineDate: String,
    val currentInspectionDate: String,
    val baselineInspectionPhoto: List<String>,
    val currentInspectionPhoto: List<String>,
    val changes: List<ItemCompareUi>,
    val totalChanges: Int
) {
    fun getDifferenceInDays(): Long {
        val formatter = DateTimeFormatter.ofPattern(
            "MMM dd, yyyy",
            Locale.ENGLISH
        )
        val firstDate = LocalDate.parse(baselineDate, formatter)
        val secondDate = LocalDate.parse(currentInspectionDate, formatter)
        val daysBetween = ChronoUnit.DAYS.between(
            firstDate,
            secondDate
        )
        return abs(daysBetween)
    }
}

data class ItemCompareUi(
    val side: CarSide,
    val label: String,
    val type: DamageChangeType
) {
    fun getNameOfSideAndIssue(): String {
        return side.nameSide() + " " + label.replaceFirstChar { it.uppercase() }
    }
}

fun InspectionComparison.toCompareUi(): CompareUi {
    val dateTimeBaseline = LocalDateTime.parse(baselineDate)
    val dateTimeCurrentInspection = LocalDateTime.parse(currentInspectionDate)
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
    return CompareUi(
        baselineDate = dateTimeBaseline.format(formatter),
        currentInspectionDate = dateTimeCurrentInspection.format(formatter),
        baselineInspectionPhoto = baselineInspectionPhoto,
        currentInspectionPhoto = currentInspectionPhoto,
        changes = changes.map { it.toItemCompareUi() },
        totalChanges = changes.size
    )
}

fun DamageComparison.toItemCompareUi() = ItemCompareUi(
    side = side,
    label = label,
    type = type
)