package com.example.mycar.model.inspection

import android.net.Uri
import com.example.domain.model.inspection.CarSide

data class InspectionPhotoStep(
    val side: CarSide,
    val photoUri: Uri? = null
)