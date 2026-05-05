package com.example.mycar.fragments.vehicleinspection

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.UploadInspectionUseCase
import com.example.mycar.model.inspection.InspectionCaptureUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InspectionCaptureViewModel @Inject constructor(
    private val uploadInspectionUseCase: UploadInspectionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InspectionCaptureUiState())
    val uiState: StateFlow<InspectionCaptureUiState> = _uiState.asStateFlow()

    fun setVehicleId(vehicleId: String) {
        _uiState.update { it.copy(vehicleId = vehicleId) }
    }

    fun saveCapturedPhoto(uri: Uri) {
        _uiState.update { state ->
            val updated = state.steps.toMutableList()
            updated[state.currentStepIndex] =
                updated[state.currentStepIndex].copy(photoUri = uri)

            state.copy(steps = updated)
        }
    }

    fun retakeCurrentPhoto() {
        _uiState.update { state ->
            val updated = state.steps.toMutableList()
            updated[state.currentStepIndex] =
                updated[state.currentStepIndex].copy(photoUri = null)

            state.copy(steps = updated)
        }
    }

    fun goToNextStep() {
        _uiState.update { state ->
            if (state.currentStepIndex >= state.steps.lastIndex) state
            else state.copy(currentStepIndex = state.currentStepIndex + 1)
        }
    }

    fun isLastStep(): Boolean = _uiState.value.isLastStep

    fun uploadInspection(
        onSuccess: (String) -> Unit,
        onError: (String?) -> Unit
    ) {
        val state = _uiState.value

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isUploading = true) }

                val photos = state.steps
                    .filter { it.photoUri != null }
                    .associate { it.side to it.photoUri!!.path!! }

                val result = uploadInspectionUseCase(
                    vehicleId = state.vehicleId!!,
                    photos = photos
                )

                _uiState.update { it.copy(isUploading = false) }

                onSuccess(result.id)

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isUploading = false) }
                onError(e.message)
            }
        }
    }
}