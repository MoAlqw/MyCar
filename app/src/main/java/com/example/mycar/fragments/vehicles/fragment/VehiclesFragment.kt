package com.example.mycar.fragments.vehicles.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycar.R
import com.example.mycar.databinding.FragmentVehiclesBinding
import com.example.mycar.fragments.BaseFragment
import com.example.mycar.fragments.vehicledetails.fragment.VehicleDetailsFragment
import com.example.mycar.fragments.vehicles.adapter.VehiclesAdapter
import com.example.mycar.fragments.vehicles.viemodel.VehiclesViewModel
import com.example.mycar.fragments.vehiclesadd.AddVehicleFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VehiclesFragment : BaseFragment<FragmentVehiclesBinding>(
    FragmentVehiclesBinding::inflate
) {

    private val viewModel: VehiclesViewModel by viewModels()

    private val vehiclesAdapter by lazy {
        VehiclesAdapter { vehicle ->
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.main, VehicleDetailsFragment.Companion.newInstance(vehicle.id))
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeVehicles()
        setupClicks()
    }

    private fun setupRecyclerView() {
        binding.rvVehicles.layoutManager = LinearLayoutManager(requireContext())
        binding.rvVehicles.adapter = vehiclesAdapter
    }

    private fun observeVehicles() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.vehicles.collectLatest { vehicles ->
                when (vehicles.size) {
                    0 -> {
                        binding.tvNoVehicles.visibility = View.VISIBLE
                        binding.rvVehicles.visibility = View.GONE
                    }
                    else -> {
                        binding.tvNoVehicles.visibility = View.GONE
                        binding.rvVehicles.visibility = View.VISIBLE
                        vehiclesAdapter.submitList(vehicles)
                    }
                }
            }
        }
    }

    private fun setupClicks() {
        binding.fabAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.main, AddVehicleFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}