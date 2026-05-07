package com.example.mycar.fragments.vehicledetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.comparison.InspectionComparison
import com.example.domain.model.comparison.addedCount
import com.example.domain.model.comparison.removedCount
import com.example.domain.usecase.CompareInspectionUseCase
import com.example.domain.usecase.DeleteVehicleUseCase
import com.example.domain.usecase.GetAllInspectionsOfVehicleUseCase
import com.example.domain.usecase.GetBaselineUseCase
import com.example.domain.usecase.GetVehicleDetailsUseCase
import com.example.domain.usecase.SetBaselineUseCase
import com.example.domain.usecase.SyncInspectionsUseCase
import com.example.mycar.model.inspection.toUi
import com.example.mycar.model.vehicle.VehicleDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VehicleDetailsViewModel @Inject constructor(
    private val getVehicleDetailsUseCase: GetVehicleDetailsUseCase,
    private val deleteVehicleUseCase: DeleteVehicleUseCase,
    private val getAllInspectionsOfVehicleUseCase: GetAllInspectionsOfVehicleUseCase,
    private val syncInspectionsUseCase: SyncInspectionsUseCase,
    private val getBaselineUseCase: GetBaselineUseCase,
    private val setBaselineUseCase: SetBaselineUseCase,
    private val compareInspectionUseCase: CompareInspectionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VehicleDetailsUiState())
    val uiState: StateFlow<VehicleDetailsUiState> = _uiState.asStateFlow()

    private val _vehicleDeleted = MutableSharedFlow<Unit>()
    val vehicleDeleted: SharedFlow<Unit> = _vehicleDeleted.asSharedFlow()

    private var observeVehicleJob: Job? = null

    fun loadVehicle(vehicleId: String) {
        observeVehicleJob?.cancel()

        observeVehicleJob = viewModelScope.launch {
            try {
                _uiState.value = VehicleDetailsUiState(isLoading = true)
                syncInspectionsUseCase(vehicleId)
                val vehicle = getVehicleDetailsUseCase(vehicleId)
                combine(
                    getAllInspectionsOfVehicleUseCase(vehicleId),
                    getBaselineUseCase(vehicleId)
                ) { inspections, baselineId ->
                    VehicleDetailsUiState(
                        vehicle = vehicle,
                        inspections = inspections.map {
                            val comparison = compareInspection(baselineId, it.id)
                            it.toUi().copy(
                                addedIssues = comparison?.addedCount() ?: 0,
                                removedIssues = comparison?.removedCount() ?: 0,
                                isBaseline = it.id == baselineId
                            )
                        },
                        isLoading = false
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = VehicleDetailsUiState(
                    isLoading = false,
                    error = e.message
                        ?: "Failed to load vehicle details"
                )
            }
        }
    }

    fun setBaseline(vehicleId: String, inspectionId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setBaselineUseCase(vehicleId, inspectionId)
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

    fun inspectionBaselineId(): String {
        return  _uiState.value.inspections.find { it.isBaseline }?.id ?: ""
    }

    private suspend fun compareInspection(baselineId: String?, currentInspectionId: String): InspectionComparison? {
        return withContext(Dispatchers.IO) {
            return@withContext if (baselineId != null && baselineId != currentInspectionId) {
                compareInspectionUseCase(baselineId, currentInspectionId)
            } else {
                null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        observeVehicleJob?.cancel()
    }
}