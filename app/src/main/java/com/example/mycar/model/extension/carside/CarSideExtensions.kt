package com.example.mycar.model.extension.carside

import com.example.domain.model.inspection.CarSide
import com.example.mycar.R

fun CarSide.nameSide(): Int = when (this) {
    CarSide.FRONT -> R.string.front_name
    CarSide.REAR -> R.string.rear_name
    CarSide.LEFT -> R.string.left_name
    CarSide.RIGHT -> R.string.right_name
}

fun CarSide.title(): Int = when (this) {
    CarSide.FRONT -> R.string.front_view_name
    CarSide.REAR -> R.string.rear_view_name
    CarSide.LEFT -> R.string.left_view_name
    CarSide.RIGHT -> R.string.right_view_name
}

fun CarSide.instruction(): Int = when (this) {
    CarSide.FRONT -> R.string.front_instruction
    CarSide.REAR -> R.string.rear_instruction
    CarSide.LEFT -> R.string.left_instruction
    CarSide.RIGHT -> R.string.right_instruction
}