package com.example.mycar.fragments.vehicleinspection

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mycar.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InspectionFlowFragment : Fragment(R.layout.fragment_container) {

    private val viewModel: InspectionCaptureViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vehicleId = requireArguments().getString(VEHICLE_ID, "")
        viewModel.setVehicleId(vehicleId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            openCamera()
        }
    }

    fun openCamera() {
        childFragmentManager.beginTransaction()
            .replace(R.id.container, InspectionCameraFragment())
            .commit()
    }

    fun openPreview() {
        childFragmentManager.beginTransaction()
            .replace(R.id.container, InspectionPhotoPreviewFragment())
            .addToBackStack(null)
            .commit()
    }

    fun goBack() {
        childFragmentManager.popBackStack()
    }

    fun finishFlow(inspectionId: String) {
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY,
            bundleOf(RESULT_COMPLETED to inspectionId)
        )
        parentFragmentManager.popBackStack()
    }

    companion object {
        private const val VEHICLE_ID = "vehicle_id"

        const val REQUEST_KEY = "inspection_result"
        const val RESULT_COMPLETED = "completed"

        fun newInstance(vehicleId: String) =
            InspectionFlowFragment().apply {
                arguments = bundleOf(VEHICLE_ID to vehicleId)
            }
    }
}