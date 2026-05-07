package com.example.domain.comparator

import com.example.domain.model.comparison.InspectionComparison
import com.example.domain.model.inspection.Inspection

interface InspectionComparator {

    fun compare(
        baseline: Inspection,
        current: Inspection
    ): InspectionComparison
}