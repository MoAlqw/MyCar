package com.example.mycar.fragments.compare

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.example.mycar.R
import com.example.mycar.databinding.FragmentInspectionDetailsContainerBinding
import com.example.mycar.fragments.BaseFragment
import com.example.mycar.model.type.PageType
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InspectionDetailsContainerFragment : BaseFragment<FragmentInspectionDetailsContainerBinding>(
    FragmentInspectionDetailsContainerBinding::inflate
) {

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        val inspectionId =
            requireArguments().getString(INSPECTION_ID, "")

        val baselineId = requireArguments().getString(BASELINE_ID, "")

        val pages = if (inspectionId == baselineId) {
            listOf(PageType.ANALYSIS)
        } else {
            listOf(PageType.ANALYSIS, PageType.COMPARISON)
        }

        binding.viewPager.adapter =
            InspectionPagerAdapter(
                fragment = this,
                pages = pages,
                inspectionId = inspectionId,
                baselineId = baselineId
            )

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (pages[position]) {
                PageType.ANALYSIS -> getString(R.string.analysis)
                PageType.COMPARISON -> getString(R.string.comparison)
            }
        }.attach()

        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        private const val INSPECTION_ID = "inspection_id"
        private const val BASELINE_ID = "baseline_id"

        fun newInstance(inspectionId: String, baselineId: String) =
            InspectionDetailsContainerFragment().apply {
                arguments = bundleOf().apply {
                    putString(INSPECTION_ID, inspectionId)
                    putString(BASELINE_ID, baselineId)
                }
            }
    }
}