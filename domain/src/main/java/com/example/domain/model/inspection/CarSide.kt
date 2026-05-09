package com.example.domain.model.inspection

enum class CarSide {
    FRONT,
    REAR,
    LEFT,
    RIGHT
}

fun String.toCarSide(): CarSide {
    return when (this.lowercase()) {
        CarSide.FRONT.name.lowercase() -> CarSide.FRONT
        CarSide.REAR.name.lowercase() -> CarSide.REAR
        CarSide.LEFT.name.lowercase() -> CarSide.LEFT
        CarSide.RIGHT.name.lowercase() -> CarSide.RIGHT
        else -> error("Unknown side: $this")
    }
}