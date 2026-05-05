package com.example.mycar.fragments.inspection.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetInspectionUseCase
import com.example.mycar.model.inspection.InspectionOfVehicleUi
import com.example.mycar.model.inspection.toInspectionOfVehicleUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InspectionOfVehicleViewModel @Inject constructor(
    private val getInspectionUseCase: GetInspectionUseCase
) : ViewModel() {

    private val _inspection = MutableStateFlow<InspectionOfVehicleUi?>(null)
    val inspection: StateFlow<InspectionOfVehicleUi?> = _inspection.asStateFlow()

    fun loadInspection(inspectionId: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.Default) {
                    val inspection = getInspectionUseCase(inspectionId)
                    _inspection.value = inspection.toInspectionOfVehicleUi()
                }
            } catch (e: CancellationException) {
                throw e
            }
            // TODO(): handle error
        }
    }
}