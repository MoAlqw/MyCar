package com.example.domain.model.inspection

enum class CarSide {
    FRONT,
    REAR,
    LEFT,
    RIGHT
}

fun CarSide.title(): String = when (this) {
    CarSide.FRONT -> "Front View"
    CarSide.REAR -> "Rear View"
    CarSide.LEFT -> "Left View"
    CarSide.RIGHT -> "Right View"
}

fun CarSide.instruction(): String = when (this) {
    CarSide.FRONT -> "Align the front of the car within the silhouette."
    CarSide.REAR -> "Align the rear of the car within the silhouette."
    CarSide.LEFT -> "Align the left side of the car within the silhouette."
    CarSide.RIGHT -> "Align the right side of the car within the silhouette."
}

fun String.toCarSide(): CarSide {
    return when (this.lowercase()) {
        "front" -> CarSide.FRONT
        "rear" -> CarSide.REAR
        "left" -> CarSide.LEFT
        "right" -> CarSide.RIGHT
        else -> error("Unknown side: $this")
    }
}