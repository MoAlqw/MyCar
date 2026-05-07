package com.example.mycar.fragments.vehiclesadd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.vehicle.Vehicle
import com.example.domain.usecase.AddVehicleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddVehicleViewModel @Inject constructor(
    private val addVehicleUseCase: AddVehicleUseCase
) : ViewModel() {
    private val _vehicleAdded = MutableSharedFlow<Unit>()
    val vehicleAdded = _vehicleAdded.asSharedFlow()

    fun addVehicle(vehicle: Vehicle) {
        viewModelScope.launch(Dispatchers.IO) {
            addVehicleUseCase(vehicle)
            _vehicleAdded.emit(Unit)
        }
    }
}