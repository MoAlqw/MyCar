package com.example.mycar.fragments.inspection.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.domain.model.inspection.CarSide
import com.example.mycar.BuildConfig
import com.example.mycar.R
import com.example.mycar.databinding.FragmentInspectionDetailsBinding
import com.example.mycar.fragments.BaseFragment
import com.example.mycar.fragments.inspection.adapter.InspectionOfVehicleAdapter
import com.example.mycar.fragments.inspection.viewmodel.InspectionOfVehicleViewModel
import com.example.mycar.model.inspection.InspectionOfVehicleUi
import com.example.mycar.model.inspection.InspectionSideUi
import com.example.mycar.model.inspection.toIssuesDetectionsUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InspectionOfVehicleFragment: BaseFragment<FragmentInspectionDetailsBinding>(
    FragmentInspectionDetailsBinding::inflate
) {

    private val viewModel: InspectionOfVehicleViewModel by viewModels()

    private val inspectionOfVehicleAdapter by lazy {
        InspectionOfVehicleAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inspectionId = requireArguments().getString(INSPECTION_ID, "")
        viewModel.loadInspection(inspectionId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeInspection()
    }

    private fun observeInspection() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.inspection.collect { inspection ->
                    when (inspection) {
                        null -> showLoading()
                        else -> {
                            setUi(inspection)
                            showContent()
                        }
                    }
                }
            }
        }
    }

    private fun setUi(inspection: InspectionOfVehicleUi) {
        bindBasicInfo(inspection)
        loadCarImages(inspection.sides)
        bindIssuesSection(inspection)
    }

    private fun bindBasicInfo(inspection: InspectionOfVehicleUi) {
        binding.tvTotalIssues.text = inspection.totalDamageCount.toString()
        binding.tvConfidence.text = getString(R.string.confidence_percent, inspection.confidence)
    }

    private fun loadCarImages(sides: List<InspectionSideUi>) {
        val sideMap = sides.associateBy { it.side }

        loadSideImage(binding.ivFrontSide, sideMap[CarSide.FRONT.name])
        loadSideImage(binding.ivBackSide, sideMap[CarSide.REAR.name])
        loadSideImage(binding.ivRightSide, sideMap[CarSide.RIGHT.name])
        loadSideImage(binding.ivLeftSide, sideMap[CarSide.LEFT.name])
    }

    private fun loadSideImage(imageView: ImageView, sideData: InspectionSideUi?) {
        sideData?.annotatedPhotoUrl?.let { url ->
            Glide.with(imageView)
                .load(BuildConfig.BASE_URL + url)
                .into(imageView)
        }
    }

    private fun bindIssuesSection(inspection: InspectionOfVehicleUi) {
        val hasIssues = inspection.totalDamageCount > 0

        if (hasIssues) {
            inspectionOfVehicleAdapter.submitList(inspection.toIssuesDetectionsUi())
            binding.rvDetectedIssues.visibility = View.VISIBLE
            binding.cwNoDetectionsIssues.visibility = View.GONE
        } else {
            binding.rvDetectedIssues.visibility = View.GONE
            binding.cwNoDetectionsIssues.visibility = View.VISIBLE
        }
    }

    private fun setupAdapter() {
        binding.rvDetectedIssues.adapter = inspectionOfVehicleAdapter
    }

    private fun showLoading() {
        binding.shimmerContainer.visibility = View.VISIBLE
        binding.shimmerContainer.startShimmer()
        binding.scrollView.visibility = View.GONE
    }

    private fun showContent() {
        binding.shimmerContainer.stopShimmer()
        binding.shimmerContainer.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE
    }

    companion object {
        private const val INSPECTION_ID = "inspection_id"

        fun newInstance(inspectionId: String): InspectionOfVehicleFragment {
            return InspectionOfVehicleFragment().apply {
                arguments = Bundle().apply {
                    putString(INSPECTION_ID, inspectionId)
                }
            }
        }
    }
}