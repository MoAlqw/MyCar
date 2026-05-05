package com.example.mycar.fragments.vehicles.viemodel

import androidx.lifecycle.ViewModel
import com.example.domain.model.Vehicle
import com.example.domain.usecase.ObserveVehiclesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class VehiclesViewModel @Inject constructor(
    private val observeVehiclesUseCase: ObserveVehiclesUseCase
): ViewModel() {
    val vehicles: Flow<List<Vehicle>> = observeVehiclesUseCase()
}