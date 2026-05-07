package com.example.mycar.fragments.compare

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.mycar.BuildConfig
import com.example.mycar.R
import com.example.mycar.databinding.FragmentInspectionComparisonBinding
import com.example.mycar.fragments.BaseFragment
import com.example.mycar.model.inspection.CompareUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InspectionComparisonFragment: BaseFragment<FragmentInspectionComparisonBinding>(
    FragmentInspectionComparisonBinding::inflate
) {

    private val viewModel: InspectionComparisonViewModel by viewModels()

    private val inspectionComparisonAdapter by lazy {
        InspectionComparisonAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadCompare(
            requireArguments().getString(BASELINE_ID, ""),
            requireArguments().getString(INSPECTION_ID, "")
        )

        observeCompare()
    }

    private fun observeCompare() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiCompare.collect { compare ->
                    when (compare) {
                        null -> showLoading()
                        else -> {
                            setUi(compare)
                            stopLoading()
                        }
                    }
                }
            }
        }
    }

    private fun setUi(compare: CompareUi) {
        bindBasicInfo(compare)
        loadImages(compare)
        setupAdapter(compare)
    }

    private fun setupAdapter(compare: CompareUi) {
        binding.rvDetectedIssues.adapter = inspectionComparisonAdapter
        inspectionComparisonAdapter.submitList(compare.changes)
    }

    private fun loadImages(compare: CompareUi) {
        val imageViewsBaseline = listOf(
            binding.ivFrontSideBaseline,
            binding.ivLeftSideBaseline,
            binding.ivRightSideBaseline,
            binding.ivBackSideBaseline
        )
        val urlsBaseline = compare.baselineInspectionPhoto

        val pairsBaseline = imageViewsBaseline.zip(urlsBaseline)
        pairsBaseline.forEach { (imageView, url) ->
            Glide.with(imageView)
                .load(BuildConfig.BASE_URL + url)
                .into(imageView)
        }

        val imageViewsInspection = listOf(
            binding.ivFrontSideInspection,
            binding.ivLeftSideInspection,
            binding.ivRightSideInspection,
            binding.ivBackSideInspection
        )
        val urlsInspection = compare.currentInspectionPhoto

        val pairsInspection = imageViewsInspection.zip(urlsInspection)
        pairsInspection.forEach { (imageView, url) ->
            Glide.with(imageView)
                .load(BuildConfig.BASE_URL + url)
                .into(imageView)
        }
    }

    private fun bindBasicInfo(compare: CompareUi) {
        binding.tvCompareIssuesDetected.text =
            getString(R.string.anomalies_detected, compare.totalChanges)
        binding.tvTotalDifferenceIssues.text = getString(R.string.new_issues, compare.totalChanges)

        binding.tvBackDateBaseline.text = compare.baselineDate
        binding.tvLeftDateBaseline.text = compare.baselineDate
        binding.tvRightDateBaseline.text = compare.baselineDate
        binding.tvFrontDateBaseline.text = compare.baselineDate

        binding.tvBackDateInspection.text = compare.currentInspectionDate
        binding.tvLeftDateInspection.text = compare.currentInspectionDate
        binding.tvRightDateInspection.text = compare.currentInspectionDate
        binding.tvFrontDateInspection.text = compare.currentInspectionDate

        val diff = compare.getDifferenceInDays()
        val stringDiff = if (diff < 2) {
            getString(R.string.day, diff)
        } else {
            getString(R.string.days, diff)
        }

        binding.tvInspectionGap.text = stringDiff

        if (compare.totalChanges < 1) {
            binding.cwNoDetectionsIssues.visibility = View.VISIBLE
        } else {
            binding.cwNoDetectionsIssues.visibility = View.GONE
        }
    }

    private fun showLoading() {
        binding.shimmerContainer.visibility = View.VISIBLE
        binding.shimmerContainer.startShimmer()
    }

    private fun stopLoading() {
        binding.shimmerContainer.stopShimmer()
        binding.shimmerContainer.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE
    }

    companion object {
        private const val INSPECTION_ID = "inspection_id"
        private const val BASELINE_ID = "baseline_id"
        fun newInstance(inspectionId: String, baselineId: String): InspectionComparisonFragment {
            return InspectionComparisonFragment().apply {
                arguments = Bundle().apply {
                    putString(INSPECTION_ID, inspectionId)
                    putString(BASELINE_ID, baselineId)
                }
            }
        }
    }
}