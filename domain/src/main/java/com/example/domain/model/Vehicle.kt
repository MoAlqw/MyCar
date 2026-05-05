package com.example.domain.model

data class Vehicle(
    val id: String,
    val make: String,
    val model: String,
    val year: Int,
    val plate: String,
    val vin: String
)