package com.example.mycar.fragments.vehicledetails.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.DeleteVehicleUseCase
import com.example.domain.usecase.GetAllInspectionsOfVehicleUseCase
import com.example.domain.usecase.GetVehicleDetailsUseCase
import com.example.domain.usecase.SyncInspectionsUseCase
import com.example.mycar.model.inspection.toUi
import com.example.mycar.model.vehicle.VehicleDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleDetailsViewModel @Inject constructor(
    private val getVehicleDetailsUseCase: GetVehicleDetailsUseCase,
    private val deleteVehicleUseCase: DeleteVehicleUseCase,
    private val getAllInspectionsOfVehicleUseCase: GetAllInspectionsOfVehicleUseCase,
    private val syncInspectionsUseCase: SyncInspectionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VehicleDetailsUiState())
    val uiState: StateFlow<VehicleDetailsUiState> = _uiState.asStateFlow()

    private val _vehicleDeleted = MutableSharedFlow<Unit>()
    val vehicleDeleted: SharedFlow<Unit> = _vehicleDeleted.asSharedFlow()

    fun loadVehicle(vehicleId: String) {
        viewModelScope.launch {
            _uiState.value = VehicleDetailsUiState(isLoading = true)
            try {
                syncInspectionsUseCase(vehicleId)

                val vehicle = getVehicleDetailsUseCase(vehicleId)

                getAllInspectionsOfVehicleUseCase(vehicleId)
                    .map { inspections -> inspections.map { it.toUi() } }
                    .collect { inspections ->
                        _uiState.value = VehicleDetailsUiState(
                            vehicle = vehicle,
                            inspections = inspections,
                            isLoading = false
                        )
                    }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.d("MyTag", "Exception: $e")
                _uiState.value = VehicleDetailsUiState(
                    isLoading = false,
                    error = e.message ?: "Failed to load vehicle details"
                )
            }
        }
    }

    fun deleteVehicle() {
        val vehicleId = _uiState.value.vehicle?.id ?: return
        viewModelScope.launch {
            try {
                deleteVehicleUseCase(vehicleId)
                _vehicleDeleted.emit(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to delete vehicle"
                )
            }
        }
    }
}