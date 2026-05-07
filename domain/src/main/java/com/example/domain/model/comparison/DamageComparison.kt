package com.example.domain.model.comparison

import com.example.domain.model.inspection.CarSide
import com.example.domain.model.inspection.Detection

data class DamageComparison(
    val side: CarSide,
    val label: String,
    val type: DamageChangeType,
    val baselineDetection: Detection?,
    val currentDetection: Detection?,
)