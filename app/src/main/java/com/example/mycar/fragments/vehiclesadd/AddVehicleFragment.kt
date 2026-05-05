package com.example.mycar.fragments.vehiclesadd

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.model.Vehicle
import com.example.mycar.R
import com.example.mycar.databinding.FragmentAddVehicleBinding
import com.example.mycar.fragments.BaseFragment
import com.example.mycar.fragments.vinscanner.VinScannerFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class AddVehicleFragment : BaseFragment<FragmentAddVehicleBinding>(
    FragmentAddVehicleBinding::inflate
) {

    private val viewModel: AddVehicleViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        observeViewModel()
    }

    private fun setListeners() {
        binding.btnCompleteVehicleSetup.setOnClickListener {
            submitVehicle()
        }

        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.tilVin.setEndIconOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.main, VinScannerFragment())
                .addToBackStack(null)
                .commit()
        }

        parentFragmentManager.setFragmentResultListener(
            VinScannerFragment.REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val vin = bundle.getString(VinScannerFragment.RESULT_KEY_VIN).orEmpty()
            binding.etVin.setText(vin)
            binding.etVin.setSelection(vin.length)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.vehicleAdded.collect {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun submitVehicle() {
        if (!checkData()) return

        val vehicle = createVehicle()
        viewModel.addVehicle(vehicle)
    }

    private fun createVehicle(): Vehicle {
        return Vehicle(
            id = UUID.randomUUID().toString(),
            make = binding.etMake.text.toString().trim(),
            model = binding.etModel.text.toString().trim(),
            year = binding.etYear.text.toString().toInt(),
            vin = binding.etVin.text.toString().trim(),
            plate = binding.etLicensePlate.text.toString().trim()
        )
    }

    private fun checkData(): Boolean {
        clearErrors()

        if (binding.etMake.text?.isBlank() == true) {
            binding.tilMake.error = "Make is required"
            return false
        }

        if (binding.etModel.text?.isBlank() == true) {
            binding.tilModel.error = "Model is required"
            return false
        }

        if (binding.etLicensePlate.text?.isBlank() == true) {
            binding.tilLicensePlate.error = "License plate is required"
            return false
        }

        if (binding.etVin.text?.isBlank() == true) {
            binding.tilVin.error = "VIN is required"
            return false
        }

        if (binding.etYear.text?.isBlank() == true) {
            binding.tilYear.error = "Year is required"
            return false
        }

        return true
    }

    private fun clearErrors() {
        binding.tilMake.error = null
        binding.tilModel.error = null
        binding.tilLicensePlate.error = null
        binding.tilVin.error = null
        binding.tilYear.error = null
    }
}