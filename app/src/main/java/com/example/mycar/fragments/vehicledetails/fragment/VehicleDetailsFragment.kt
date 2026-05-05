package com.example.mycar.fragments.vehicledetails.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.Vehicle
import com.example.mycar.R
import com.example.mycar.databinding.FragmentVehicleDetailsBinding
import com.example.mycar.fragments.BaseFragment
import com.example.mycar.fragments.inspection.fragment.InspectionOfVehicleFragment
import com.example.mycar.fragments.vehicledetails.adapter.VehicleInspectionsAdapter
import com.example.mycar.fragments.vehicledetails.viewmodel.VehicleDetailsViewModel
import com.example.mycar.fragments.vehicleinspection.InspectionFlowFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VehicleDetailsFragment : BaseFragment<FragmentVehicleDetailsBinding>(
    FragmentVehicleDetailsBinding::inflate
) {

    private val viewModel: VehicleDetailsViewModel by viewModels()

    private val inspectionsAdapter by lazy {
        VehicleInspectionsAdapter { inspectionId ->
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.main, InspectionOfVehicleFragment.newInstance(inspectionId))
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vehicleId = requireArguments().getString(VEHICLE_ID, "")
        viewModel.loadVehicle(vehicleId)

        setupRecyclerView()
        setupToolbar()
        observeUiState()
        observeVehicleDeleted()
        listenerOfInspection()
    }

    private fun setupRecyclerView() {
        binding.rvInspections.layoutManager = LinearLayoutManager(requireContext())
        binding.rvInspections.adapter = inspectionsAdapter
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_delete_vehicle -> {
                    viewModel.deleteVehicle()
                    true
                }

                else -> false
            }
        }

        binding.btnStartInspection.setOnClickListener {
            viewModel.uiState.value.vehicle?.id?.let { vehicleId ->
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                    .replace(R.id.main, InspectionFlowFragment.newInstance(vehicleId))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    renderLoading(state.isLoading)

                    state.error?.let { error ->
                        Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
                    }

                    state.vehicle?.let { vehicle ->
                        setUi(vehicle)
                    }

                    inspectionsAdapter.submitList(state.inspections)
                }
            }
        }
    }

    private fun observeVehicleDeleted() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.vehicleDeleted.collect {
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun renderLoading(isLoading: Boolean) = with(binding) {
        shimmerContainer.isVisible = isLoading
        scrollView.isVisible = !isLoading

        if (isLoading) {
            shimmerContainer.startShimmer()
        } else {
            shimmerContainer.stopShimmer()
        }
    }

    private fun listenerOfInspection() {
        parentFragmentManager.setFragmentResultListener(
            InspectionFlowFragment.REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val inspectionId = bundle.getString(InspectionFlowFragment.RESULT_COMPLETED)
            if (inspectionId != null) {
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                    .replace(R.id.main, InspectionOfVehicleFragment.newInstance(inspectionId))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun setUi(vehicle: Vehicle) = with(binding) {
        tvVehicleTitle.text = "${vehicle.year} ${vehicle.make} ${vehicle.model}"
        tvVin.text = vehicle.vin
    }

    companion object {
        private const val VEHICLE_ID = "vehicle_id"

        fun newInstance(vehicleId: String): VehicleDetailsFragment {
            return VehicleDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(VEHICLE_ID, vehicleId)
                }
            }
        }
    }
}