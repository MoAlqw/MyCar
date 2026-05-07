package com.example.mycar.fragments.compare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.CompareInspectionUseCase
import com.example.mycar.model.inspection.CompareUi
import com.example.mycar.model.inspection.toCompareUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InspectionComparisonViewModel @Inject constructor(
    private val compareInspectionUseCase: CompareInspectionUseCase
): ViewModel() {

    private val _uiCompare = MutableStateFlow<CompareUi?>(null)
    val uiCompare: StateFlow<CompareUi?> = _uiCompare

    fun loadCompare(baselineId: String, currentId: String) {
        viewModelScope.launch {
            val compare = compareInspectionUseCase(baselineId, currentId)
            _uiCompare.value = compare.toCompareUi()
        }
    }
}