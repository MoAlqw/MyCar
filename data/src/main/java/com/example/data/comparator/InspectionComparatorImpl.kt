package com.example.data.comparator

import com.example.domain.comparator.InspectionComparator
import com.example.domain.model.comparison.DamageChangeType
import com.example.domain.model.comparison.DamageComparison
import com.example.domain.model.comparison.InspectionComparison
import com.example.domain.model.inspection.BoundingBox
import com.example.domain.model.inspection.CarSide
import com.example.domain.model.inspection.Detection
import com.example.domain.model.inspection.Inspection
import javax.inject.Inject

class InspectionComparatorImpl @Inject constructor() : InspectionComparator {

    override fun compare(
        baseline: Inspection,
        current: Inspection
    ): InspectionComparison {

        val changes = mutableListOf<DamageComparison>()
        val baselinePhotos = mutableListOf<String>()
        val currentPhotos = mutableListOf<String>()
        baseline.sides.forEach {
            baselinePhotos += it.annotatedPhotoUrl
        }
        current.sides.forEach {
            currentPhotos += it.annotatedPhotoUrl
        }
        CarSide.entries.forEach { side ->

            val baselineSide = baseline.sides.firstOrNull {
                it.side == side
            }

            val currentSide = current.sides.firstOrNull {
                it.side == side
            }

            val baselineDetections =
                baselineSide?.detections.orEmpty()

            val currentDetections =
                currentSide?.detections.orEmpty()

            changes += compareSide(
                side = side,
                baselineDetections = baselineDetections,
                currentDetections = currentDetections
            )
        }

        return InspectionComparison(
            baselineInspectionId = baseline.id,
            currentInspectionId = current.id,
            baselineDate = baseline.createdAt,
            currentInspectionDate = current.createdAt,
            baselineInspectionPhoto = baselinePhotos,
            currentInspectionPhoto = currentPhotos,
            changes = changes
        )
    }

    private fun compareSide(
        side: CarSide,
        baselineDetections: List<Detection>,
        currentDetections: List<Detection>
    ): List<DamageComparison> {

        val comparisons = mutableListOf<DamageComparison>()
        val matchedBaselineIds = mutableSetOf<String>()

        currentDetections.forEach { current ->

            val bestMatch = baselineDetections
                .filter {
                    it.label == current.label &&
                            it.id !in matchedBaselineIds
                }
                .maxByOrNull {
                    calculateIoU(it.boundingBox, current.boundingBox)
                }

            if (bestMatch != null) {

                val iou = calculateIoU(
                    bestMatch.boundingBox,
                    current.boundingBox
                )

                if (iou >= IOU_THRESHOLD) {

                    matchedBaselineIds += bestMatch.id

                    comparisons += DamageComparison(
                        side = side,
                        label = current.label,
                        type = DamageChangeType.UNCHANGED,
                        baselineDetection = bestMatch,
                        currentDetection = current
                    )

                } else {

                    comparisons += DamageComparison(
                        side = side,
                        label = current.label,
                        type = DamageChangeType.ADDED,
                        baselineDetection = null,
                        currentDetection = current
                    )
                }

            } else {

                comparisons += DamageComparison(
                    side = side,
                    label = current.label,
                    type = DamageChangeType.ADDED,
                    baselineDetection = null,
                    currentDetection = current
                )
            }
        }

        baselineDetections
            .filter { it.id !in matchedBaselineIds }
            .forEach { baseline ->

                comparisons += DamageComparison(
                    side = side,
                    label = baseline.label,
                    type = DamageChangeType.REMOVED,
                    baselineDetection = baseline,
                    currentDetection = null
                )
            }

        return comparisons
    }

    private fun calculateIoU(
        a: BoundingBox,
        b: BoundingBox
    ): Float {

        val intersectionX1 = maxOf(a.x1, b.x1)
        val intersectionY1 = maxOf(a.y1, b.y1)
        val intersectionX2 = minOf(a.x2, b.x2)
        val intersectionY2 = minOf(a.y2, b.y2)

        val intersectionWidth =
            maxOf(0f, intersectionX2 - intersectionX1)

        val intersectionHeight =
            maxOf(0f, intersectionY2 - intersectionY1)

        val intersectionArea =
            intersectionWidth * intersectionHeight

        val areaA =
            (a.x2 - a.x1) * (a.y2 - a.y1)

        val areaB =
            (b.x2 - b.x1) * (b.y2 - b.y1)

        val unionArea =
            areaA + areaB - intersectionArea

        if (unionArea <= 0f) {
            return 0f
        }

        return intersectionArea / unionArea
    }

    private companion object {
        const val IOU_THRESHOLD = 0.35f
    }
}